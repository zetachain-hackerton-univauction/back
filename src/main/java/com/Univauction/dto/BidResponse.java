package com.Univauction.dto;

import java.time.LocalDateTime;

public record BidResponse(
        Integer bidId,
        Integer bidderUserId,
        String bidderWalletAddress,
        String tokenSymbol,
        Double bidAmount,
        String comment,
        String bidToken,
        LocalDateTime createdAt
) {}