package com.tripple.entity;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EventDTO {

    private String action;

    private String type;

    private String userid;

    private String placeid;

    private String reviewid;

    private String content;

    private String[] attachedPhotoIds;

}

