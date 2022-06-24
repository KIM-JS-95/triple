package com.tripple.entity;


import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @Column(name = "PLACE_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Review> reviews = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "PLACE_ID")
//    private User user;


//    public void setUser(User user) {
//        this.user = user;
//        if (!user.getPlaces().contains(this)) {
//            user.getPlaces().add(this);
//        }
//    }
}
