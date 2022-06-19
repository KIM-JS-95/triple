package com.tripple.entity;

import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Service;

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
public class User {

    @Id
    @Column(name = "USER_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @ColumnDefault("0")
    private int point;

    // TODO: 연관관계 주인인 FK를 관리한다.
    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private List<Place> places= new ArrayList<>();
//

    // what: 리뷰 저장 시 자동 매핑
    public void addReiew(Review review) {
        this.reviews.add(review);
        review.setUser(this);
    }

}
