package com.tripple.service;

import com.tripple.entity.*;
import com.tripple.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class TripleService {

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
    public boolean add(EventDTO dto) {

        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        Review find_review = reviewRepository.findByPlaceIdAndUserId(dto.getPlaceid(), dto.getUserid());
        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();
        Long find_place_count = placeRepository.count();

        int point = 0;
        int photo_size = dto.getAttachedPhotoIds().size();
        /*
        최초 리뷰일 경우 보너스 점수 부여
         */
        if (find_place_count == 0L) {
            point++;
        }
        if (photo_size >= 1 && dto.getContent().length() >= 1) {
            point += 2;
        } else if (photo_size >= 1 || dto.getContent().length() >= 1) {
            point++;
        }

        if (find_user != null && find_place != null) {
            if (find_review == null) {

                Review review = Review.builder()
                        .id(dto.getReviewid())
                        .content(dto.getContent())
                        .build();

                find_user.addReiew(review);
                review.setUser(find_user);
                review.setPlace(find_place);
                //find_place.setReviews(review);

                //TODO: 이미지 저장
                for (UUID photo_UUID : dto.getAttachedPhotoIds()) {

                    Photo mockphoto = Photo.builder()
                            .attachedPhotoIds(photo_UUID)
                            .build();

                    mockphoto.setReview(review);
                    review.addPhoto(mockphoto);

                    photoRepository.save(mockphoto);
                }
                reviewRepository.save(review);
            }
        }

        PointLog pointLog = PointLog.builder()
                .point(point + "가 적립되셨습니다.")
                .build();
        find_user.setPoint(point);
        find_user.addPointLog(pointLog);
        pointLog.setUser(find_user);
        userRepository.save(find_user);
        logRepository.save(pointLog);
        return true;

    }


    /*
       TODO:  mod 리뷰 수정
         수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수한다.
         사진 글자수 다시 체크할 것
    */
    public boolean mod(EventDTO dto) {


        Review mockuser = reviewRepository.findByIdAndUserId(dto.getReviewid(), dto.getUserid());

        mockuser.setContent(dto.getContent());
        reviewRepository.save(mockuser);

        return true;
    }


    /*
       TODO: delete 리뷰 삭제
         리뷰로 부여한 점수와 보너스 점수를 회수한다.
    */
    public boolean del(EventDTO dto) {

        Review find_review = reviewRepository.findById(dto.getReviewid()).orElseThrow();
        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();

        Optional<Review> review = reviewRepository.findById(dto.getReviewid());

        if (!review.isEmpty()) {
            reviewRepository.deleteById(dto.getReviewid());

            // 포인트 처리
            int photo_size = find_review.getPhotos().size();
            int content_size = find_review.getContent().length();

            String rating = find_review.getRating();
            int user_point = find_user.getPoint();

            if (photo_size >= 1 && content_size >= 1) {
                user_point -= 2;
            } else if (photo_size >= 1 || content_size >= 1) {
                user_point--;
            }

            if (rating == "first") {
                user_point--;
            }

            if (user_point < 0) {
                user_point = 0;
            }

            find_user.setPoint(user_point);

            PointLog pointLog = PointLog.builder()
                    .point(user_point + "가 차감되셨습니다.")
                    .build();

            find_user.addPointLog(pointLog);
            pointLog.setUser(find_user);

            userRepository.save(find_user);
            logRepository.save(pointLog);
            return true;
        }
        return false;

    }


}
