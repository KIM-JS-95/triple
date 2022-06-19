package com.tripple.controller;

import com.tripple.entity.Photo;
import com.tripple.entity.Place;
import com.tripple.entity.Review;
import com.tripple.entity.User;
import com.tripple.repository.PhotoRepository;
import com.tripple.repository.PlaceRepository;
import com.tripple.repository.ReviewRepository;
import com.tripple.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@SpringBootTest
class pointcontollerTest {




}