package com.Univauction.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record AuctionCardResponse(
        Integer auctionId,
        String filesUrl,
        String title,
        String summary,
        String tags,
        Double currentBid,
        Long bidCount,
        LocalDateTime endDate,
        Long secondsLeft
) {
    public static AuctionCardResponse of(Integer auctionId, String filesUrl, String title, String summary, String tags,
                                         Double currentBid, Long bidCount, LocalDateTime endDate) {
        long left = endDate != null ? Math.max(0, Duration.between(LocalDateTime.now(), endDate).getSeconds()) : 0;
        return new AuctionCardResponse(auctionId, filesUrl, title, summary, tags, currentBid, bidCount, endDate, left);
    }
}
