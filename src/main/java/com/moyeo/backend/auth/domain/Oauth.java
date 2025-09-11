package com.moyeo.backend.auth.domain;

import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_oauth")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false, unique = true)
    private String oauthId;
}
