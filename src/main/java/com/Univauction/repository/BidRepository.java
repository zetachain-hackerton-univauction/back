package com.Univauction.repository;

import com.Univauction.domain.Bid;
import com.Univauction.domain.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    // 리스트 반환(기존)
    List<Bid> findByAuctionOrderByBidAmountDesc(Auction auction);
    List<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);

    Page<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction , Pageable pageable);
    Page<Bid> findByAuctionOrderByBidAmountDesc(Auction auction, Pageable pageable);

    long countByAuction(Auction auction);

}
