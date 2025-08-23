package com.Univauction.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auctionId;

    @ManyToOne
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "highest_bidder_id")
    private User highestBidder;

    private Double highestBid;

    private LocalDateTime endDate; // D-day, 남은 시간 계산 필요

    private String status; // OPEN or CLOSED

    private String licenseType;   // "EXCLUSIVE" or "NON_EXCLUSIVE"

    private Double reservePrice;  // 리저브 가격
}
