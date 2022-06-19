package com.tripple.controller;


import com.tripple.entity.EventDTO;
import com.tripple.service.TripleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
