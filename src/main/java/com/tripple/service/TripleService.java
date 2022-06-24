package com.tripple.service;

import com.tripple.entity.*;
import com.tripple.repository.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Transactional
@Service
public class TripleService {

    private final String rate_first = "first";
    private final String rate_normal = "first";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private LogRepository logRepository;


    /*
       TODO: create 리뷰 작성
         한 사용자는 장소마다 리뷰를 1개만 작성할 수 있고, 리뷰는 수정 또하는 삭제할 수 있다.
         리뷰로 작성한 내용에 따라 포인트를 부여한다.
         - 내용:
            - 1자 이산의 텍스트: 1점
            - 1장 이상 사진 첨부: 1점
         - 보너스
            - 특정 장소에 첫 리뷰
    */
    public boolean add(Converted dto) {

        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        Review find_review = reviewRepository.findByPlaceIdAndUserId(dto.getPlaceid(), dto.getUserid());
        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();
        //Long find_place_count = placeRepository.count();

        //int point = find_user.getPoint();
        int new_point =0;
        int photo_size = dto.getAttachedPhotoIds().size();

        // 리뷰가 최초이려면 place 레포의 review 값이 0 이어야함
        if (find_place.getReviews().size() == 0) {
            new_point++;

        }
        if (photo_size >= 1 && dto.getContent().length() >= 1) {
            new_point += 2;

        } else if (photo_size >= 1 || dto.getContent().length() >= 1) {
            new_point++;

        }

        // 이미 존재하는 리뷰
        if (find_review != null) {
            return false;
        }

        if (find_user != null && find_place != null) {

            Review review = Review.builder()
                    .id(dto.getReviewid())
                    .content(dto.getContent())
                    .build();

            find_user.addReiew(review);
            review.setUser(find_user);
            review.setPlace(find_place);

            // 첫 댓글 기록
            if (find_place.getReviews() == null) {
                review.setRating(rate_first);
            } else {
                review.setRating(rate_normal);
            }

            //TODO: 이미지 저장
            for (UUID photo_UUID : dto.getAttachedPhotoIds()) {
                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();

                review.addPhoto(mockphoto);
                review.setUser(find_user);
                review.setPlace(find_place);

            }
            reviewRepository.save(review);

            PointLog pointLog = PointLog.builder()
                    .point("(작성) 적립 포인트" + new_point)
                    .build();

            find_user.setPoint(new_point);
            find_user.addPointLog(pointLog);
            pointLog.setUser(find_user);

            userRepository.save(find_user);
        }
        return true;
    }


    /*
       TODO:  mod 리뷰 수정
         수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수한다.
         사진 글자수 다시 체크할 것
    */
    public boolean mod(Converted dto) {

        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();
        Review find_review = reviewRepository.findByIdAndUserId(dto.getReviewid(), dto.getUserid());
        if (find_review == null) {
            return false;
        }

        find_review.getPhotos().clear();

        /*

        old_point = old_point +- (old_photo_size - new_photo_size)
         */
        int old_point = find_user.getPoint(); // 현재 유저 포인트
        int old_photo_size = find_review.getPhotos().size(); // 이전 포토 크기
        int new_photo_size = dto.getAttachedPhotoIds().size();  // 새로운 포토 크기

        int new_point = 0;
        // 리뷰가 최초이려면 place 레포의 review 값이 0 이어야함
        if (find_place.getReviews().size() == 0) {
            new_point++;
        }
        if ((old_photo_size - new_photo_size) >= 1 && dto.getContent().length() >= 1) {
            new_point += 2;
        } else if ((old_photo_size - new_photo_size) >= 1 || dto.getContent().length() >= 1) {
            new_point++;
        }

        log.info("갱신 포인트"+new_point);
        if (find_user != null && find_place != null) {

            //TODO: 이미지 저장
            for (UUID photo_UUID : dto.getAttachedPhotoIds()) {
                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();

                find_review.addPhoto(mockphoto);
            }
            reviewRepository.save(find_review);
        }

        log.info(" 포인트: " + new_point);

        PointLog pointLog = new PointLog();

        // 이전보다 포이트가 적다면?
        if (old_point > new_point) {
            old_point = old_point - (old_point - new_point);
            pointLog.setPoint("(수정): 차감 포인트" + old_point);
        } else {
            // 이전보다 포인트가 많다면?
            // old_point < new_point
            old_point = old_point + (new_point - old_point);
            pointLog.setPoint("(수정): 적립 포인트" + old_point);
        }

        find_user.setPoint(new_point);
        find_user.addPointLog(pointLog);
        userRepository.save(find_user);

        return true;
    }


    /*
       TODO: delete 리뷰 삭제
         리뷰로 부여한 점수와 보너스 점수를 회수한다.
    */
    public boolean del(Converted dto) {

        Review find_review = reviewRepository.findById(dto.getReviewid())
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        find_review.setPlace(null);
        // 포인트 처리
        int photo_size = find_review.getPhotos().size();
        int content_size = find_review.getContent().length();
        String rating = find_review.getRating();

        int point = find_user.getPoint();

        if (photo_size >= 1 && content_size >= 1) {
            point -= 2;
        } else if (photo_size >= 1 || content_size >= 1) {
            point--;
        }
        if (rating.equals("first")) {
            point--;
        }
        if (point < 0) {
            point = 0;
        }

        find_user.setPoint(point);
        PointLog pointLog = PointLog.builder()
                .point("(삭제) 갱신 포인트" + point)
                .build();

        find_user.addPointLog(pointLog);
        pointLog.setUser(find_user);

        userRepository.save(find_user);
        reviewRepository.deleteById(dto.getReviewid());
        return true;

    }

    public User find_user(UUID user_id) {

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));

        return user;
    }


}
