package com.tripple.service;

import com.tripple.entity.*;
import com.tripple.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private TripleService tripleService;

    //TODO: 신규 유저 저장 & 장소 저장
    @Order(value = 1)
    @Test
    @DisplayName("User_add")
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
    @Order(value = 2)
    @Test
    @DisplayName("Review_Test")
    public void Review_Photo() {
        EventDTO dto = converter();
        assertTrue(tripleService.add(dto));
//        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
//        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();
//
//        Long find_place_count = placeRepository.count();
//
//        User mockuser = User.builder()
//                .id(dto.getUserid())
//                .build();
//
//        Place mockplace = Place.builder()
//                .id(dto.getPlaceid())
//                .build();
//
//        int point = 0;
//        int photo_size = dto.getAttachedPhotoIds().size();
//        /*
//        최초 리뷰일 경우 보너스 점수 부여
//         */
//        if (find_place_count == 0L) {
//            point++;
//        }
//        if (photo_size >= 1 && dto.getContent().length() >= 1) {
//            point += 2;
//        } else if (photo_size >= 1 || dto.getContent().length() >= 1) {
//            point++;
//        }
//
//        if (find_user != null && find_place != null) {
//
//            Review find_review = Review.builder()
//                    .id(dto.getReviewid())
//                    .content(dto.getContent())
//                    .build();
//
//            mockuser.addReiew(find_review);
//            find_review.setUser(find_user);
//            find_review.setPlace(find_place);
//            mockplace.setReview(find_review);
//
//            reviewRepository.save(find_review);
//
//            //TODO: 이미지 저장
//            for (UUID photo_UUID : dto.getAttachedPhotoIds()) {
//
//                Photo mockphoto = Photo.builder()
//                        .attachedPhotoIds(photo_UUID)
//                        .build();
//
//                mockphoto.setReview(find_review);
//                find_review.addPhoto(mockphoto);
//
//                photoRepository.save(mockphoto);
//            }
//        }

//        PointLog pointLog = PointLog.builder()
//                .point(point + "가 적립되셨습니다.")
//                .build();
//
//        mockuser.setPoint(point);
//        mockuser.addPointLog(pointLog);
//        pointLog.setUser(mockuser);
//
//        userRepository.save(find_user);
//        logRepository.save(pointLog);

        Review mockreview = reviewRepository.findById(dto.getReviewid()).orElseThrow();

        //TODO: 포토 체크
        assertThat(mockreview.getId(), is(dto.getReviewid()));

    }

    /*
해당 유저의 리뷰를 조회 후 사진 및 수정 기능 추가
 */
    @Order(value = 3)
    @Test
    @DisplayName("Modify_Test")
    public void mod() {

        EventDTO dto = converter();

        assertTrue(tripleService.mod(dto));

//        Review mockuser = reviewRepository.findByIdAndUserId(dto.getReviewid(), dto.getUserid());
//        mockuser.setContent(dto.getContent());
//        reviewRepository.save(mockuser);

        Review mockreview = reviewRepository.findById(dto.getReviewid()).orElseThrow();
        assertThat(mockreview.getContent(), is(dto.getContent()));
    }

    //@Transactional
    @Order(value = 4)
    @Test
    @DisplayName("delete_Test")
    public void delete() {
        EventDTO dto = converter();
        //assertTrue(tripleService.del(dto));

        Place find_place = Place.builder()
                .id(dto.getPlaceid())
                .build();

        User find_user = User.builder()
                .id(dto.getUserid())
                .build();

        Review mock_review = Review.builder()
                .id(dto.getReviewid())
                .content(dto.getContent())
                .rating("first")
                .place(find_place)
                .user(find_user)
                .build();

        // 사진 저장
        for (UUID photo_UUID : dto.getAttachedPhotoIds()) {

            Photo mockphoto = Photo.builder()
                    .attachedPhotoIds(photo_UUID)
                    .review(mock_review)
                    .build();
            mockphoto.setReview(mock_review);
            mock_review.addPhoto(mockphoto);
        }

        // 리뷰저장
        Review find_review = reviewRepository.findById(dto.getReviewid()).orElseThrow();

        // 테스트 시작
        Optional<Review> review = reviewRepository.findById(dto.getReviewid());

        if (!review.isEmpty()) {

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

            reviewRepository.deleteById(dto.getReviewid());
        }
        assertThat(reviewRepository.existsById(dto.getReviewid()), is(false)); // false

        User user_assert = userRepository.findById(dto.getUserid()).orElseThrow();
        assertThat(user_assert.getPoint(), is(0));

    }


    public EventDTO converter() {
        var user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        var review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String place = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"
                , "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

        String content = "내용 수정입니다.";

        UUID user__UUID = UUID.fromString(user);
        UUID place__UUID = UUID.fromString(place);
        UUID review_UUID = UUID.fromString(review);

        List<UUID> photo_UUID = new ArrayList<>();

        for (String s : photo) {
            photo_UUID.add(UUID.fromString(s));
        }

        EventDTO dto = EventDTO.builder()
                .reviewid(review_UUID)
                .content(content)
                .attachedPhotoIds(photo_UUID)
                .userid(user__UUID)
                .placeid(place__UUID)
                .build();

        return dto;
    }
}