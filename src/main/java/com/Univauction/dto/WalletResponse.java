package com.Univauction.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {
    private Integer walletId;
    private String walletAddress;
    private String tokenName;
    private Double balance;
}
