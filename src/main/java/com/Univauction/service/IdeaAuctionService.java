package com.Univauction.service;

import com.Univauction.domain.Auction;
import com.Univauction.domain.Idea;
import com.Univauction.domain.User;
import com.Univauction.dto.IdeaAuctionCreateRequest;
import com.Univauction.dto.IdeaAuctionCreateResponse;
import com.Univauction.repository.AuctionQueryRepository;
import com.Univauction.repository.AuctionRepository;
import com.Univauction.repository.IdeaRepository;
import com.Univauction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeaAuctionService {

    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final AuctionRepository auctionRepository;

    @Transactional
    public IdeaAuctionCreateResponse createIdeaWithAuction(IdeaAuctionCreateRequest req) {
        // 1) 사용자 확인
        User owner = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.getUserId()));

        // 2) Idea 저장
        Idea idea = new Idea();
        idea.setUser(owner);
        idea.setTitle(req.getTitle());
        idea.setSummary(req.getSummary());
        idea.setCategory(req.getCategory());
        idea.setTags(req.getTags());

        // 파일 URL 리스트를 콤마로 직렬화 (해커톤용 빠른 방식)
        List<String> fileUrls = req.getFileUrls();
        idea.setFilesUrl(fileUrls == null || fileUrls.isEmpty() ? null : String.join(",", fileUrls));

        idea.setProblemStatement(req.getProblemStatement());
        idea.setSolutionOverview(req.getSolutionOverview());
        idea.setMarketOpportunity(req.getMarketOpportunity());
        idea.setCompetitiveAdvantage(req.getCompetitiveAdvantage());

        Idea savedIdea = ideaRepository.save(idea);

        // 3) Auction 저장
        LocalDateTime endDate = calcEndDate(req.getAuctionDuration());
        Auction auction = new Auction();
        auction.setIdea(savedIdea);
        auction.setSeller(owner);
        auction.setHighestBidder(null);
        auction.setHighestBid(req.getStartingBid() == null ? 0.0 : req.getStartingBid());
        auction.setEndDate(endDate);
        auction.setStatus("OPEN"); // 생성 시 오픈

        // 확장 컬럼
        auction.setLicenseType(req.getLicenseType());  // "EXCLUSIVE" | "NON_EXCLUSIVE"
        auction.setReservePrice(req.getReservePrice());

        Auction savedAuction = auctionRepository.save(auction);

        // 4) 응답
        IdeaAuctionCreateResponse res = new IdeaAuctionCreateResponse();
        res.setIdeaId(savedIdea.getIdeaId());
        res.setAuctionId(savedAuction.getAuctionId());
        return res;
    }

    private LocalDateTime calcEndDate(String duration) {
        if (duration == null) {
            throw new IllegalArgumentException("auctionDuration is required");
        }
        return switch (duration.toUpperCase()) {
            case "3 Days" -> LocalDateTime.now().plusDays(3);
            case "7 Days"  -> LocalDateTime.now().plusDays(7);
            case "14 Days"  -> LocalDateTime.now().plusDays(14);
            case "30 Days"  -> LocalDateTime.now().plusDays(30);
            default -> {
                try {
                    if (duration.endsWith("H")) {
                        int h = Integer.parseInt(duration.substring(0, duration.length() - 1));
                        yield LocalDateTime.now().plusHours(h);
                    } else if (duration.endsWith("D")) {
                        int d = Integer.parseInt(duration.substring(0, duration.length() - 1));
                        yield LocalDateTime.now().plusDays(d);
                    }
                } catch (Exception ignore) { }
                throw new IllegalArgumentException("Invalid auctionDuration: " + duration);
            }
        };
    }
}
