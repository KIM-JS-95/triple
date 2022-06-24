package com.tripple.entity;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Converted {

    private UUID userid;

    private UUID placeid;

    private UUID reviewid;

    private String content;

    private List<UUID> attachedPhotoIds;

}

