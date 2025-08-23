package com.Univauction.dto;

import com.Univauction.domain.Wallet;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponseDto {
    private Integer walletId;
    private Integer userId;
    private String walletAddress;
    private String tokenName;
    private Double balance;

    public WalletResponseDto(Wallet wallet) {
        this.walletId = wallet.getWalletId();
        this.userId = wallet.getUser().getUserId();
        this.walletAddress = wallet.getWalletAddress();
        this.tokenName = wallet.getTokenName();
        this.balance = wallet.getBalance();
    }
}
