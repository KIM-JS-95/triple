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

    public void add(EventDTO dto) {
        String user = dto.getUserid();
        String review = dto.getReviewid();
        String place = dto.getPlaceid();

        UUID user_UUID = UUID.fromString(user);
        UUID review_UUID = UUID.fromString(review);
        UUID place_UUID = UUID.fromString(place);

        User mockuser = userRepository.findById(user_UUID).orElseThrow();
        Place mockplace = placeRepository.findById(place_UUID).orElseThrow();

        String[] photo = dto.getAttachedPhotoIds();

        if (mockuser != null && mockplace != null) {

            Review mockreview = Review.builder()
                    .id(review_UUID)
                    .content("좋아요!")
                    .build();

            mockuser.addReiew(mockreview);
            mockreview.setUser(mockuser);

            mockreview.setPlace(mockplace);
            mockplace.setReview(mockreview);

            reviewRepository.save(mockreview);

            //TODO: 이미지 저장
            for (String s : photo) {
                UUID photo_UUID = UUID.fromString(s);

                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();

                mockphoto.setReview(mockreview);
                mockreview.addPhoto(mockphoto);

                photoRepository.save(mockphoto);
            }

        }

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
        UUID uuid_review = UUID.fromString(review);
        UUID uuid_place = UUID.fromString(place);


        Review review1 = reviewRepository.findByid(uuid_review);




        reviewRepository.deleteById(uuid_review);

        return true;

    }


}
