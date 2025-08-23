package com.Univauction.repository;
import com.Univauction.domain.Auction;
import com.Univauction.dto.AuctionSort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuctionQueryRepositoryImpl implements AuctionQueryRepository {

    private final EntityManager em;

    @Override
    public Page<Auction> findActiveAuctions(
            String keyword,
            List<String> categories,
            AuctionSort sort,
            Pageable pageable
    ) {
        // ---------- 공통 WHERE ----------
        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        // 활성 + 마감 안지남
        where.append(" a.status = :openStatus ");
        params.put("openStatus", "OPEN");

        where.append(" and a.endDate > :now ");
        params.put("now", LocalDateTime.now());

        // 키워드(제목/요약/태그)
        if (keyword != null && !keyword.isBlank()) {
            where.append(" and (")
                    .append(" lower(i.title) like :kw ")
                    .append(" or lower(i.summary) like :kw ")
                    .append(" or lower(i.tags) like :kw ")
                    .append(") ");
            params.put("kw", "%" + keyword.toLowerCase() + "%");
        }

        // 카테고리 IN
        if (categories != null && !categories.isEmpty()) {
            where.append(" and i.category in :categories ");
            params.put("categories", categories);
        }

        // ---------- SELECT 본문 ----------
        // Bid 컬렉션을 Auction에 매핑 안했다 가정 → ON으로 조인
        // MOST_BIDS 정렬 지원 위해 항상 left join b 하고 group by a
        String select =
                "select a " +
                        "from Auction a " +
                        "join a.idea i " +
                        "left join Bid b on b.auction = a ";

        String groupBy = " group by a ";

        String orderBy = buildOrderBy(sort);

        String jpql = select + " where " + where + groupBy + orderBy;

        TypedQuery<Auction> query = em.createQuery(jpql, Auction.class);
        params.forEach(query::setParameter);

        // 페이지네이션
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Auction> content = query.getResultList();

        // ---------- COUNT 쿼리 ----------
        // group by가 있으니 count(distinct a)로 개수 계산
        String countJpql =
                "select count(distinct a) " +
                        "from Auction a " +
                        "join a.idea i " +
                        "left join Bid b on b.auction = a " +
                        "where " + where;

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        params.forEach(countQuery::setParameter);
        long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    private String buildOrderBy(AuctionSort sort) {
        // JPQL에서 COUNT(b) 정렬은 group by 후 order by count(b) 가능
        // nulls last는 JPQL 표준엔 없음 → 보통 DB가 정함. 필요시 highestBid는 coalesce 사용
        if (sort == null) sort = AuctionSort.LATEST;

        return switch (sort) {
            case HIGHEST_BID -> " order by coalesce(a.highestBid, 0) desc ";
            case MOST_BIDS   -> " order by count(b) desc ";
            case ENDING_SOON -> " order by a.endDate asc ";
            case LATEST      -> " order by a.auctionId desc ";
        };
    }
}