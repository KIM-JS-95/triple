package com.tripple.entity;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @Column(name = "REVIEW_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private String rating;

    @Builder.Default
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "PLACE_ID")
    private Place place;


    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setReview(this);
    }

}
