package com.Univauction.repository;

import com.Univauction.domain.Auction;
import com.Univauction.dto.AuctionSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AuctionQueryRepository {
    Page<Auction> findActiveAuctions(
            String keyword,
            List<String> categories,
            AuctionSort sort,
            Pageable pageable
    );
}

