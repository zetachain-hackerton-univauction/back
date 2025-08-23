package com.Univauction.service;

import com.Univauction.domain.Auction;
import com.Univauction.domain.Bid;
import com.Univauction.domain.User;
import com.Univauction.domain.Wallet;
import com.Univauction.dto.BidResponse;
import com.Univauction.repository.AuctionRepository;
import com.Univauction.repository.BidRepository;
import com.Univauction.repository.UserRepository;
import com.Univauction.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public BidResponse placeBid(Integer auctionId,
                                Integer userId,
                                String tokenSymbol,
                                Double bidAmount,
                                String comment) {

        // 1) 엔티티 조회
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        User bidder = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2) 상태/시간/금액 유효성 체크
        if (!"OPEN".equalsIgnoreCase(auction.getStatus())) {
            throw new IllegalStateException("이 옥션은 입찰할 수 없습니다 (status=" + auction.getStatus() + ")");
        }
        if (auction.getEndDate() != null && auction.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("이미 종료된 옥션입니다.");
        }

        double currentHighest = auction.getHighestBid() == null ? 0.0 : auction.getHighestBid();
        if (bidAmount == null || bidAmount <= currentHighest) {
            throw new IllegalArgumentException("입찰 금액은 현재 최고가(" + currentHighest + ")보다 커야 합니다.");
        }

        // 3) 입찰자 지갑 주소(첫 번째 지갑 사용) — 없으면 null
        String walletAddress = walletRepository
                .findTopByUserOrderByWalletIdAsc(bidder)
                .map(Wallet::getWalletAddress)
                .orElse(null);

        // 4) Bid 생성/저장
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setUser(bidder);
        bid.setBidAmount(bidAmount);
        bid.setTokenSymbol(tokenSymbol);
        bid.setComment(comment);
        bid.setCreatedAt(LocalDateTime.now());
        bid.setBidderWalletAddress(walletAddress); // 엔티티에 해당 필드 존재해야 함

        Bid saved = bidRepository.save(bid);

        // 5) 최고가/최고입찰자 갱신
        auction.setHighestBid(bidAmount);
        auction.setHighestBidder(bidder);
        auctionRepository.save(auction);

        return new BidResponse(
                saved.getBidId(),
                bidder.getUserId(),
                walletAddress,
                tokenSymbol,
                bidAmount,
                comment,
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public List<BidResponse> getBids(Integer auctionId, String sort) {
        // 1) 옥션 존재 확인
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found: " + auctionId));

        // 2) 정렬 기준 정규화 (기본: 최신순)
        String key = (sort == null) ? "latest" : sort.toLowerCase();

        // 3) 정렬에 맞게 조회
        List<Bid> bids = switch (key) {
            case "amount_desc" -> bidRepository.findByAuctionOrderByBidAmountDesc(auction);
            case "latest" -> bidRepository.findByAuctionOrderByCreatedAtDesc(auction);
            default -> throw new IllegalArgumentException("Unsupported sort: " + sort);
        };

        // 4) 응답 DTO로 매핑
        return bids.stream()
                .map(b -> new BidResponse(
                        b.getBidId(),
                        b.getUser().getUserId(),                       // userId
                        String.valueOf(b.getUser().getUserId()),       // bidder(표시용) — 별도 닉네임 없으면 id 문자열로
                        b.getBidderWalletAddress(),                    // walletAddress
                        b.getBidAmount(),                              // amount
                        b.getTokenSymbol(),                            // tokenSymbol
                        b.getComment(),                                // comment
                        b.getCreatedAt()                               // timestamp
                ))
                .toList();
    }

    // ✅ 새로 추가할 Pageable 지원 버전
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
                String.valueOf(b.getUser().getUserId()),
                b.getBidderWalletAddress(),
                b.getBidAmount(),
                b.getTokenSymbol(),
                b.getComment(),
                b.getCreatedAt()
        ));
    }
}
