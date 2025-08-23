package com.Univauction.repository;

import com.Univauction.domain.Bid;
import com.Univauction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    List<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);
    List<Bid> findByAuctionOrderByBidAmountDesc(Auction auction);
    long countByAuction(Auction auction);

}
