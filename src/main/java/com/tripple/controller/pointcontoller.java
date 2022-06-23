package com.tripple.controller;


import com.tripple.entity.EventDTO;
import com.tripple.service.TripleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class pointcontoller {

    @Autowired
    private TripleService tripleService;

    @PostMapping("/events")
    public void event(@RequestBody EventDTO dto) {

        String type = dto.getType();

        if (type.equals("REVIEW")
                && dto.getAction().equals("ADD")) {
            tripleService.add(dto);
        }
    }

    public EventDTO converter() {
        var user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        var review = "240a0658-dc5f-4878-9381-ebb7b2667772";
        String place = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        String[] photo = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"
                , "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

        UUID user__UUID = UUID.fromString(user);
        UUID place__UUID = UUID.fromString(place);
        UUID review_UUID = UUID.fromString(review);

        List<UUID> photo_UUID = new ArrayList<>();

        for (String s: photo){
            photo_UUID.add(UUID.fromString(s));
        }

        EventDTO dto = EventDTO.builder()
                .reviewid(review_UUID)
                .content("좋아요!!")
                .attachedPhotoIds(photo_UUID)
                .userid(user__UUID)
                .placeid(place__UUID)
                .build();

        return dto;
    }
}
