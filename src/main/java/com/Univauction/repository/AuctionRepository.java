package com.Univauction.repository;

import com.Univauction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer>, AuctionQueryRepository {

    @Query("""
        select a from Auction a
        join fetch a.idea i
        left join fetch a.seller s
        left join fetch a.highestBidder hb
        where a.auctionId = :auctionId
    """)
    Optional<Auction> findDetailById(Integer auctionId);
}
