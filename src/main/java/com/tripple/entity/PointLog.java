package com.tripple.entity;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointLog {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String point;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;


}
