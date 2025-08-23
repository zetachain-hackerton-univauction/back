package com.Univauction.controller;

import com.Univauction.dto.AuctionCardResponse;
import com.Univauction.dto.AuctionDetailResponse;
import com.Univauction.dto.AuctionSort;
import com.Univauction.dto.BidResponse;
import com.Univauction.service.AuctionDetailService;
import com.Univauction.service.AuctionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionQueryController {

    private final AuctionQueryService auctionQueryService;
    private final AuctionDetailService auctionDetailService;

    public AuctionQueryController(AuctionQueryService auctionQueryService, AuctionDetailService auctionDetailService) {
        this.auctionQueryService = auctionQueryService;
        this.auctionDetailService = auctionDetailService;
    }

    // 전체 활성화된 옥션 목록 조회
    // 예: GET /auctions?sort=ENDING_SOON&q=ai&categories=AI,BLOCKCHAIN&page=0&size=12
    @GetMapping
    public Page<AuctionCardResponse> listActiveAuctions(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categories, // "AI,BLOCKCHAIN"
            @RequestParam(defaultValue = "LATEST") AuctionSort sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        List<String> categoryList = (categories == null || categories.isBlank())
                ? null
                : Arrays.stream(categories.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        return auctionQueryService.getActiveAuctions(q, categoryList, sort, (Pageable) PageRequest.of(page, size));
    }

    // 특정 옥션의 입찰 기록 조회 (기본: 최신순)
    // 예: GET /auctions/5/bids?sort=latest  또는 ?sort=amount_desc
    @GetMapping("/{auctionId}/bids")
    public List<BidResponse> listBids(
            @PathVariable Integer auctionId,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return auctionQueryService.getBids(auctionId, sort);
    }

    @GetMapping("/{auctionId}")
    public AuctionDetailResponse getAuctionDetail(@PathVariable Integer auctionId) {
        return auctionDetailService.getDetail(auctionId);
    }
}
