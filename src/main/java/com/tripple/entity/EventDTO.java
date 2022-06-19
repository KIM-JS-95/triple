package com.tripple.entity;


import lombok.Data;

import java.util.UUID;

@Data
public class EventDTO {

    private String action;

    private String type;

    private String userid;

    private String placeid;

    private String reviewid;

    private String content;

    private String[] attachedPhotoIds;

}

