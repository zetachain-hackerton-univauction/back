package com.Univauction.dto;

import java.time.LocalDateTime;

public record BidResponse(
        Integer bidId,
        Integer userId,
        Double bidAmount,
        String status,
        LocalDateTime createdAt) {}
