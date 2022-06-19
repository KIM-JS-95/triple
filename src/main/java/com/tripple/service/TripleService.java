package com.tripple.service;

import com.tripple.entity.EventDTO;
import com.tripple.entity.Photo;
import com.tripple.entity.Review;
import com.tripple.entity.User;
import com.tripple.repository.PhotoRepository;
import com.tripple.repository.ReviewRepository;
import com.tripple.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TripleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PhotoRepository photoRepository;


    public void add(EventDTO dto) {
        String user = dto.getUserid();
        String review = dto.getReviewid();

        UUID user_UUID = UUID.fromString(user);
        UUID review_UUID = UUID.fromString(review);

        User mockuser = userRepository.findById(user_UUID).orElseThrow();

        String[] photo = dto.getAttachedPhotoIds();

        if (mockuser != null) {

            Review mockreview = Review.builder()
                    .id(review_UUID)
                    .content("좋아요!")
                    .build();

            mockreview.setUser(mockuser);
            mockuser.addReiew(mockreview);

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

            reviewRepository.save(mockreview);
        }

    }



    //TODO: MOD
    public void mod(EventDTO dto) {


    }

    //TODO: delete 리뷰 삭제
    public void del(EventDTO dto) {
        String user = dto.getUserid();
        UUID uuid_user = UUID.fromString(user);

        reviewRepository.findByUser(uuid_user);

    }


}
