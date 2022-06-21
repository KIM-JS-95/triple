package com.tripple.entity;


import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @Column(name = "PLACE_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY)
    private Review review;

//    @ManyToOne
//    @JoinColumn(name = "PLACE_ID")
//    private User user;

    public void setReview(Review review) {
        this.review = review;
    }

//    public void setUser(User user) {
//        this.user = user;
//        if (!user.getPlaces().contains(this)) {
//            user.getPlaces().add(this);
//        }
//    }
}
