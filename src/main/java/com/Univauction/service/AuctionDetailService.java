package com.Univauction.service;

import com.Univauction.domain.Auction;
import com.Univauction.dto.AuctionDetailResponse;
import com.Univauction.repository.AuctionRepository;
import com.Univauction.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionDetailService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public AuctionDetailResponse getDetail(Integer auctionId) {
        Auction a = auctionRepository.findDetailById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        // files_url -> List<String>
        String filesUrl = a.getIdea().getFilesUrl(); // 콤마(,)로 직렬화되어 있다고 가정
        List<String> files = (filesUrl == null || filesUrl.isBlank())
                ? List.of()
                : Arrays.stream(filesUrl.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        long secondsLeft = 0L;
        if (a.getEndDate() != null) {
            secondsLeft = Duration.between(LocalDateTime.now(), a.getEndDate()).getSeconds();
            if (secondsLeft < 0) secondsLeft = 0;
        }

        long totalBidders = bidRepository.countByAuction(a);

        return new AuctionDetailResponse(
                a.getAuctionId(),
                a.getIdea().getTitle(),
                a.getIdea().getSummary(),
                a.getIdea().getTags(),
                a.getIdea().getProblemStatement(),
                a.getIdea().getSolutionOverview(),
                a.getIdea().getMarketOpportunity(),
                a.getIdea().getCompetitiveAdvantage(),
                files,
                a.getHighestBid(),
                a.getReservePrice(),   // null 가능
                totalBidders,
                a.getEndDate(),
                secondsLeft
        );
    }
}
