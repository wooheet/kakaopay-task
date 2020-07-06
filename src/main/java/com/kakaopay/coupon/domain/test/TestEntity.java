package com.kakaopay.coupon.domain.test;

import lombok.*;

import javax.persistence.*;

@Table(name = "test")
@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestEntity {
    @Id
    @Column(name = "test_id")
    private String num;
}
