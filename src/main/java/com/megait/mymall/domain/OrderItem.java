package com.megait.mymall.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem { //오더랑 아이템 테이블 중간에 매핑 한 것!
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Item item;  //상품

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order; //주문

    private int orderPrice; //구매가

    private int count; // 수량

}
