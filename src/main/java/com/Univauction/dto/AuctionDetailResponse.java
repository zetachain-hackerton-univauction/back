package com.Univauction.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AuctionDetailResponse(
        Integer auctionId,

        // Idea
        String title,
        String summary,
        String tags,
        String problemStatement,
        String solutionOverview,
        String marketOpportunity,
        String competitiveAdvantage,
        List<String> files,          // files_url을 리스트로 변환

        // Auction
        Double currentBid,           // 현재 최고 입찰가 (auction.highestBid)
        Double reservePrice,         // 판매자 설정 리저브 가격(있다면)
        Long totalBidders,           // 총 입찰자 수
        LocalDateTime endDate,
        Long secondsLeft             // 남은 초(0 미만이면 0)
) {}
