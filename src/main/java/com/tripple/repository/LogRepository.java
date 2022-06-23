package com.tripple.repository;

import com.tripple.entity.Photo;
import com.tripple.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<PointLog, Long> {

}
