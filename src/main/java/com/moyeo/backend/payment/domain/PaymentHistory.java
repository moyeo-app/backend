package com.moyeo.backend.payment.domain;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_history")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(nullable = false, unique = true)
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderId;
}
