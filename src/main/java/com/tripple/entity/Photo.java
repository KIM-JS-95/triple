package com.tripple.entity;


import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @Column(name = "ATTACHEDPHOTO", columnDefinition = "BINARY(16)")
    private UUID attachedPhotoIds;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID")
    private Review review;

    public void setReview(Review review) {
        this.review = review;
        if (!review.getPhotos().contains(this)) {
            review.getPhotos().add(this);
        }
    }

}
