package com.Univauction.controller;

import com.Univauction.dto.BidResponse;
import com.Univauction.dto.PlaceBidRequest;
import com.Univauction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    // 입찰 생성
    @PostMapping("/bids/{auctionId}")
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable Integer auctionId,
            @RequestBody PlaceBidRequest request
    ) {
        BidResponse response = bidService.placeBid(
                auctionId,
                request.getUserId(),       // ✅ 여기서 userId 꺼냄
                request.getTokenSymbol(),
                request.getBidAmount(),
                request.getComment()
        );
        return ResponseEntity.ok(response);
    }

    // 입찰 목록 조회 (latest | amount_desc)
    @GetMapping("/bids/{auctionId}")
    public Page<BidResponse> getBids(@PathVariable Integer auctionId,
                                     @RequestParam(required = false, defaultValue = "latest") String sort,
                                     Pageable pageable) {
        return bidService.getBids(auctionId, sort, pageable);
    }
}