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

    private final String rate = "final";

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
        //assertTrue(tripleService.add(dto));

        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();

        User mockuser = User.builder()
                .id(dto.getUserid())
                .point(0)
                .build();

        Place mockplace = Place.builder()
                .id(dto.getPlaceid())
                .build();

        /*
        최초 리뷰일 경우 보너스 점수 부여
         */
        int point = mockuser.getPoint();
        int photo_size = dto.getAttachedPhotoIds().size();

        // 리뷰가 최초이려면 place 레포의 review 값이 0 이어야함
        if (mockplace.getReviews() == null) {
            point++;
        }
        if (photo_size >= 1 && dto.getContent().length() >= 1) {
            point += 2;
        } else if (photo_size >= 1 || dto.getContent().length() >= 1) {
            point++;
        }

        if (find_user != null && find_place != null) {

            Review find_review = Review.builder()
                    .id(dto.getReviewid())
                    .content(dto.getContent())
                    .rating(rate)
                    .build();

//            mockuser.addReiew(find_review);
//            find_review.setUser(find_user);
//            find_review.setPlace(find_place);
//            mockplace.setReview(find_review);
//
//            reviewRepository.save(find_review);

            //TODO: 이미지 저장
            for (UUID photo_UUID : dto.getAttachedPhotoIds()) {
                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();
//                mockphoto.setReview(find_review);
//                find_review.addPhoto(mockphoto);
                find_review.addPhoto(mockphoto);
                find_review.setUser(find_user);
                find_review.setPlace(find_place);
                //photoRepository.save(mockphoto);

                // 리뷰가 두번 저장되는데 어떻게좀 해봐.
            }
            reviewRepository.save(find_review);
        }

        System.out.println("적립 포인트: " + point);

        PointLog pointLog = PointLog.builder()
                .point(point + "가 적립되셨습니다.")
                .build();

        mockuser.setPoint(point);
        mockuser.addPointLog(pointLog);
        mockuser.addPlace(find_place);

        //pointLog.setUser(mockuser);

        userRepository.save(mockuser);
        //logRepository.save(pointLog);

        Review mockreview = reviewRepository.findById(dto.getReviewid()).orElseThrow();

        //TODO: 포토 체크
        assertThat(mockreview.getId(), is(dto.getReviewid()));

    }


    @Order(value = 3)
    @Test
    @DisplayName("Modify_Test")
    public void mod() {

        EventDTO dto = converter();

        User find_user = userRepository.findById(dto.getUserid()).orElseThrow();
        Place find_place = placeRepository.findById(dto.getPlaceid()).orElseThrow();

        User mockuser = User.builder()
                .id(dto.getUserid())
                .point(0)
                .build();

        Place mockplace = Place.builder()
                .id(dto.getPlaceid())
                .build();

        //assertTrue(tripleService.mod(dto));
   /*
        해당 유저의 리뷰를 조회 후 사진 및 수정 기능 추가
        사진 및 글자수 재 계산
 */
        Review mockreview = reviewRepository.findByIdAndUserId(dto.getReviewid(), dto.getUserid());
        System.out.println(mockreview.getPhotos().size());
        mockreview.getPhotos().clear();

        System.out.println("이미지 크기는: " + dto.getAttachedPhotoIds().size());

        int point = mockuser.getPoint();
        int photo_size = dto.getAttachedPhotoIds().size();

        // 리뷰가 최초이려면 place 레포의 review 값이 0 이어야함
        if (mockplace.getReviews() == null) {
            point++;
        }
        if (photo_size >= 1 && dto.getContent().length() >= 1) {
            point += 2;
        } else if (photo_size >= 1 || dto.getContent().length() >= 1) {
            point++;
        }

        if (find_user != null && find_place != null) {

            //TODO: 이미지 저장
            for (UUID photo_UUID : dto.getAttachedPhotoIds()) {
                Photo mockphoto = Photo.builder()
                        .attachedPhotoIds(photo_UUID)
                        .build();

                mockreview.addPhoto(mockphoto);


            }
            reviewRepository.save(mockreview);
        }

        Review assert_review = reviewRepository.findById(dto.getReviewid()).orElseThrow();
        assertThat(assert_review.getPhotos().size(), is(2));
    }


    /*
    Cascade 속성으로, 리뷰 삭제시 이미지 또한 자동으로 삭제됨
     */

    @Order(value = 4)
    @Test
    @DisplayName("delete_Test")
    public void delete() {
        EventDTO dto = converter();
        //assertTrue(tripleService.del(dto));

        // GIVEN

        User find_user = User.builder()
                .id(dto.getUserid())
                .build();

        Place find_place = Place.builder()
                .id(dto.getPlaceid())
                .build();

        Review mock_review = Review.builder()
                .id(dto.getReviewid())
                .content(dto.getContent())
                .rating("first")
                .place(find_place)
                .user(find_user)
                .build();

        // 사진 삭제
        for (UUID photo_UUID : dto.getAttachedPhotoIds()) {

            Photo mockphoto = Photo.builder()
                    .attachedPhotoIds(photo_UUID)
                    .review(mock_review)
                    .build();

            mockphoto.setReview(mock_review);
            mock_review.addPhoto(mockphoto);
        }

        // 리뷰저장
        Review find_review = reviewRepository.save(mock_review);

        // 테스트 시작
        Optional<Review> review = reviewRepository.findById(dto.getReviewid());
        User mockuser = userRepository.findById(dto.getUserid()).orElseThrow();

        if (!review.isEmpty()) {

            // 포인트 처리
            int photo_size = find_review.getPhotos().size();
            int content_size = find_review.getContent().length();
            String rating = find_review.getRating();

            int point = mockuser.getPoint();
            System.out.println("현재 포인트: " + point);
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

            System.out.println("차감 포인트: " + point);
            find_user.setPoint(point);

            PointLog pointLog = PointLog.builder()
                    .point(point + "가 차감되셨습니다.")
                    .build();

            find_user.addPointLog(pointLog);
            pointLog.setUser(find_user);

            userRepository.save(find_user);
            //logRepository.save(pointLog);

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
        //String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"};
        String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

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