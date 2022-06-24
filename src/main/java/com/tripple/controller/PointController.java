package com.tripple.controller;


import com.tripple.entity.Converted;
import com.tripple.entity.Event;
import com.tripple.entity.User;
import com.tripple.service.TripleService;
import net.bytebuddy.utility.nullability.AlwaysNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PointController {

    private TripleService tripleService;

    // POST / events 로 호출하는 포인트 적립 API
    @PostMapping("/events")
    public boolean event(@RequestBody Event event) {

        String type = event.getType();

        /*
        String to UUID 형 변환
         */
        Converted dto = converter(event);

        boolean result = false;
        if (type.equals("REVIEW") && event.getAction().equals("ADD")) {
            result = tripleService.add(dto);
        }else if(type.equals("REVIEW") && event.getAction().equals("MOD")){
            result = tripleService.mod(dto);
        }else if(type.equals("REVIEW") && event.getAction().equals("DELETE")){
            result = tripleService.del(dto);
        }
        return result;
    }

    // 포인트, 조회 API
    @GetMapping("/user")
    public Map<String, Integer> event(@RequestParam("user") String user_id) {
        UUID user__UUID = UUID.fromString(user_id);

        User user = tripleService.find_user(user__UUID);
        Map<String,Integer> map = new HashMap<>();
        map.put("point",user.getPoint());
        return map;
    }

    public Converted converter(Event dto) {

        UUID user__UUID = UUID.fromString(dto.getUserid());
        UUID place__UUID = UUID.fromString(dto.getPlaceid());
        UUID review_UUID = UUID.fromString(dto.getReviewid());

        List<UUID> photo_UUID = new ArrayList<>();
        for (String s : dto.getAttachedPhotoIds()) {
            photo_UUID.add(UUID.fromString(s));
        }

        Converted converted = Converted.builder()
                .content(dto.getContent())
                .attachedPhotoIds(photo_UUID)
                .userid(user__UUID)
                .placeid(place__UUID)
                .reviewid(review_UUID)
                .build();

        return converted;
    }

}
