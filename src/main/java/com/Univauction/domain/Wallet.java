package com.Univauction.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(name = "uk_wallet_address", columnNames = "wallet_address"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Integer walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wallet_user"))
    private User user;

    @Column(unique = true, nullable = false)
    private String walletAddress;

    @Column(name = "token_name")
    private String tokenName;

    private Double balance;
}