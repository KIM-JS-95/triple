package com.tripple.service;

import com.tripple.entity.*;
import com.tripple.repository.PhotoRepository;
import com.tripple.repository.PlaceRepository;
import com.tripple.repository.ReviewRepository;
import com.tripple.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        String user = dto.getUserid();
        String review = dto.getReviewid();
        String place = dto.getPlaceid();
        String content = dto.getContent();
        String[] photo = dto.getAttachedPhotoIds();

        UUID user_UUID = UUID.fromString(user);
        UUID review_UUID = UUID.fromString(review);
        UUID place_UUID = UUID.fromString(place);

        User find_user = userRepository.findById(user_UUID).orElseThrow();
        Place find_place = placeRepository.findById(place_UUID).orElseThrow();

        Long find_place_count = placeRepository.count();


        int point=0;
        int photo_size = photo.length;
        /*
        최초 리뷰일 경우 보너스 점수 부여
         */
        if(find_place_count == 0L){
            point++;
        }
        if (photo_size>=1 && content.length()>=1){
            point +=2;
        }else if(photo_size>=1 || content.length()>=1){
            point ++;
        }

        if (find_user != null && find_place != null) {

            Review find_review = Review.builder()
                    .id(review_UUID)
                    .content(content)
                    .build();

            find_user.addReiew(find_review);
            find_review.setUser(find_user);
            find_review.setPlace(find_place);
            find_place.setReview(find_review);

            reviewRepository.save(find_review);

            //TODO: 이미지 저장
            for (String s : photo) {
                UUID photo_UUID = UUID.fromString(s);

                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();

                mockphoto.setReview(find_review);
                find_review.addPhoto(mockphoto);

                photoRepository.save(mockphoto);
            }

            find_user.setPoint(point);
            userRepository.save(find_user);
        }

        return true;

    }


    /*
       TODO:  mod 리뷰 수정
         수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수한다.

    */
    public void mod(EventDTO dto) {


    }

    /*
       TODO: delete 리뷰 삭제
         리뷰로 부여한 점수와 보너스 점수를 회수한다.

    */
    @Transactional
    public boolean del(EventDTO dto) {
        String user = dto.getUserid();
        String review = dto.getReviewid();
        String place = dto.getPlaceid();

        UUID uuid_user = UUID.fromString(user);
        UUID uuid_review = UUID.fromString(review);
        UUID uuid_place = UUID.fromString(place);

        Review find_review = reviewRepository.findById(uuid_review).orElseThrow();
        User find_user = userRepository.findById(uuid_user).orElseThrow();

        int photo_size = find_review.getPhotos().size();
        int content_size = find_review.getContent().length();
        String rating = find_review.getRating();
        int user_point  = find_user.getPoint();

        if (photo_size>=1 && content_size>=1){
            user_point -= 2;
        }else if(photo_size>=1 || content_size>=1){
            user_point--;
        }

        if(rating == "first"){
            user_point--;
        }

        reviewRepository.delete(find_review);
        find_user.setPoint(user_point);

        return true;

    }


}
