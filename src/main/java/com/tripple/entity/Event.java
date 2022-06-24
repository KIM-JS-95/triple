package com.tripple.entity;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Event {

    private String action;

    private String type;

    private String userid;

    private String placeid;

    private String reviewid;

    private String content;

    private List<String> attachedPhotoIds;

}

