package com.Univauction.dto;

public enum AuctionSort {
    LATEST,        // 최신순(auction_id desc 대용)
    HIGHEST_BID,   // 최고가
    MOST_BIDS,     // 입찰 수 많은 순
    ENDING_SOON    // 마감 임박
}
