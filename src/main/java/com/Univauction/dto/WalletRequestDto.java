package com.Univauction.dto;

import lombok.*;

//지갑 등록시 사용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRequestDto {
    private String walletAddress;
    private String tokenName;
}
