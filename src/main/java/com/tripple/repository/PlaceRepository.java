package com.tripple.repository;

import com.tripple.entity.Place;
import com.tripple.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {

}
