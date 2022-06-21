package com.tripple.repository;

import com.tripple.entity.Photo;
import com.tripple.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    void removeByReviewId(UUID uuid_review);
}
