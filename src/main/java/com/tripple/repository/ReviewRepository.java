package com.tripple.repository;

import com.tripple.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Review findByid(UUID uuid_review);

    int countByPlaceId(UUID place_uuid);
}
