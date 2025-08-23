package com.Univauction.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidId;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bid_amount", nullable = false)
    private Double bidAmount;

    @Column(name = "token_symbol")
    private String tokenSymbol; // e.g., ZETA

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "bid_token")
    private String bidToken; // 온체인 트랜잭션/서명 등

    @Column(nullable = false)
    private String status; // "PLACED", "CANCELLED" 등

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "PLACED";
    }

    @Column(name = "bidder_wallet_address", length = 200)
    private String bidderWalletAddress;

}