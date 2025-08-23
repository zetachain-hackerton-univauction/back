package com.Univauction.service;

import com.Univauction.domain.Auction;
import com.Univauction.domain.Bid;
import com.Univauction.dto.AuctionCardResponse;
import com.Univauction.dto.AuctionSort;
import com.Univauction.dto.BidResponse;
import com.Univauction.repository.AuctionRepository;
import com.Univauction.repository.BidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionQueryService {

    // ✅ AuctionQueryRepository 주입 제거
    private final AuctionRepository auctionRepository;   // 커스텀 메서드도 여기서 호출
    private final BidRepository bidRepository;

    public AuctionQueryService(AuctionRepository auctionRepository,
                               BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    public Page<AuctionCardResponse> getActiveAuctions(
            String q,
            List<String> categories,
            AuctionSort sort,
            Pageable pageable
    ) {
        List<String> catParam = (categories == null || categories.isEmpty()) ? null : categories;

        // ✅ Page<Auction> → Page<AuctionCardResponse> 매핑
        return auctionRepository.findActiveAuctions(q, catParam, sort, pageable)
                .map(a -> {
                    long bidCount = bidRepository.countByAuction(a); // BidRepository에 선언 필요
                    String filesUrl = (a.getIdea() != null) ? a.getIdea().getFilesUrl() : null; // Idea에 없다면 null

                    long secondsLeft = (a.getEndDate() != null)
                            ? java.time.Duration.between(java.time.LocalDateTime.now(), a.getEndDate()).getSeconds()
                            : 0;

                    return new AuctionCardResponse(
                            a.getAuctionId(),
                            filesUrl,
                            a.getIdea() != null ? a.getIdea().getTitle() : null,
                            a.getIdea() != null ? a.getIdea().getSummary() : null,
                            a.getIdea() != null ? a.getIdea().getTags() : null,
                            a.getHighestBid(),
                            bidCount,
                            a.getEndDate(),
                            secondsLeft
                    );
                });
    }

    public List<BidResponse> getBids(Integer auctionId, String sort) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        String key = (sort == null) ? "latest" : sort.toLowerCase();
        return switch (key) {
            case "amount_desc" -> bidRepository.findByAuctionOrderByBidAmountDesc(auction)
                    .stream()
                    .map(b -> new BidResponse(
                            b.getBidId(),
                            b.getUser().getUserId(),
                            b.getBidAmount(),
                            b.getStatus(),
                            b.getCreatedAt()))
                    .toList();
            case "latest" -> bidRepository.findByAuctionOrderByCreatedAtDesc(auction)
                    .stream()
                    .map(b -> new BidResponse(
                            b.getBidId(),
                            b.getUser().getUserId(),
                            b.getBidAmount(),
                            b.getStatus(),
                            b.getCreatedAt()))
                    .toList();
            default -> throw new IllegalArgumentException("Unsupported sort: " + sort);
        };
    }

    public Page<BidResponse> getBids(Integer auctionId, String sort, Pageable pageable) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        Page<Bid> page = switch (sort == null ? "latest" : sort.toLowerCase()) {
            case "amount_desc" -> bidRepository.findByAuctionOrderByBidAmountDesc(auction, pageable);
            case "latest" -> bidRepository.findByAuctionOrderByCreatedAtDesc(auction, pageable);
            default -> throw new IllegalArgumentException("Unsupported sort: " + sort);
        };

        return page.map(b -> new BidResponse(
                b.getBidId(),
                b.getUser().getUserId(),
                b.getBidAmount(),
                b.getStatus(),
                b.getCreatedAt()
        ));
    }
}
