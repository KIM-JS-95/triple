package com.tripple.service;

import com.tripple.entity.*;
import com.tripple.repository.PhotoRepository;
import com.tripple.repository.PlaceRepository;
import com.tripple.repository.ReviewRepository;
import com.tripple.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TripleServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PhotoRepository photoRepository;


    //TODO: 신규 유저 저장 & 장소 저장
    @Test
    @DisplayName("Review_Test")
    public void User() {
        List<Review> reviews = new ArrayList<>();

        var user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        var review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String place = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";

        UUID user__UUID = UUID.fromString(user);
        UUID place__UUID = UUID.fromString(place);

        User mockuser = User.builder()
                .id(user__UUID)
                .build();

        Place mockplace = Place.builder()
                .id(place__UUID)
                .build();

        userRepository.save(mockuser);
        placeRepository.save(mockplace);

        User u1 = userRepository.findById(user__UUID).orElseThrow();

        assertThat(u1.getId().toString(), is(user));
    }

    // TODO: 리뷰 저장
    @Test
    @DisplayName("photo_Test")
    public void Review_Photo() {

        var user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        var review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String place = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"
                , "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

        String content = "좋아요!";
        // TODO: 리뷰와 동시에 저장

        UUID user_UUID = UUID.fromString(user);
        UUID review_UUID = UUID.fromString(review);
        UUID place_UUID = UUID.fromString(place);

        //TODO: 저장된 유저 호출
        User mockuser = User.builder()
                .id(user_UUID)
                .build();

        Place mockplace = Place.builder()
                .id(place_UUID)
                .build();

         /*
                포인트 계산
                사진 1점+텍스트 1점
                둘중 하나 1점
                최초 등록 1점
        */
        int point=0;
        int photo_size = photo.length;

        if (photo_size>=1 && content.length()>=1){
            point +=2;
        }else if(photo_size>=1 || content.length()>=1){
            point ++;
        }
        int visited = reviewRepository.countByPlaceId(place_UUID);
        Review mockreview = Review.builder()
                .id(review_UUID)
                .content(content)
                .build();
        if(visited == 0){
            point++;
            mockreview.setRating("first");
        }

        //TODO; 리뷰 작성 후 저장
        if (mockuser != null && mockplace != null) {



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
                        .review(mockreview)
                        .build();

                mockphoto.setReview(mockreview);
                mockreview.addPhoto(mockphoto);

                photoRepository.save(mockphoto);
            }


            mockuser.setPoint(point);
            User savemockuser =(userRepository.save(mockuser));

            assertThat(savemockuser.getId(), is(user_UUID));
            assertThat(mockuser.getPoint(), is(3));
        }

        //TODO: 포토 체크
        assertThat(mockuser.getReviews().get(0).getId(), is(review_UUID));

    }

    @Test
    @DisplayName("delete_Test")
    public void delete() {

        String user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        String review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String place = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";

        String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"
                , "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

        UUID user__UUID = UUID.fromString(user);
        UUID place__UUID = UUID.fromString(place);
        UUID review__UUID = UUID.fromString(review);

        User mockuser = User.builder()
                .id(user__UUID)
                .point(3)
                .build();

        Place mockplace = Place.builder()
                .id(place__UUID)
                .build();


        Review mockreview = Review.builder()
                .id(review__UUID)
                .user(mockuser)
                .place(mockplace)
                .rating("first")
                .content("좋아요")
                .build();

        for (String s : photo) {

            UUID photo_UUID = UUID.fromString(s);

            Photo mockphoto = Photo.builder()
                    .attachedPhotoIds(photo_UUID)
                    .review(mockreview)
                    .build();
            mockphoto.setReview(mockreview);
            mockreview.addPhoto(mockphoto);
        }


        int photo_size = mockreview.getPhotos().size();
        int content_size = mockreview.getContent().length();
        String rating = mockreview.getRating();

        int user_point = mockuser.getPoint();
        System.out.println(user_point);

        // 최초 입력기록은 어떻게 하지?
        if (photo_size>=1 && content_size>=1){
            user_point -= 2;
        }else if(photo_size>=1 || content_size>=1){
            user_point--;
        }

        if(rating == "first"){
            user_point--;
        }

        reviewRepository.delete(mockreview);
        mockuser.setPoint(user_point);
        userRepository.save(mockuser);

        Optional<Review> deleteUser = reviewRepository.findById(review__UUID);
        assertFalse(deleteUser.isPresent()); // false

        User user_assert = userRepository.findById(user__UUID).orElseThrow();

        assertThat(mockuser.getPoint(), is(0));

    }


    /*
    해당 유저의 리뷰를 조회 후 사진 및 수정 기능 추가
     */
    @Test
    public void mod(){

        var user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        var review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        UUID user_UUID = UUID.fromString(user);
        UUID review_UUID = UUID.fromString(review);

        Review mockuser = reviewRepository.findByIdAndUserId(review_UUID, user_UUID);

        mockuser.setContent("midify content");
        mockuser.setRating("first");
        reviewRepository.save(mockuser);

        Review mockreview = reviewRepository.findById(review_UUID).orElseThrow();
        assertThat(mockreview.getContent(), is("midify content"));
        assertThat(mockreview.getRating(), is("first"));
    }
}