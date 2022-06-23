package com.tripple.repository;

import com.tripple.entity.Review;
import com.tripple.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Review findByIdAndUserId(UUID review_uuid, UUID user_uuid);

    Review findByPlaceIdAndUserId(UUID placeid, UUID userid);

}
