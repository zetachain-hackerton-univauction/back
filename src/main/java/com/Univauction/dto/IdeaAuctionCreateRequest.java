package com.Univauction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@Setter
public class IdeaAuctionCreateRequest {
    // 1 page
    @NotNull
    private Integer userId;
    @NotBlank
    private String title;
    private String summary;
    private String category;
    private String tags;

    // 2 page
    private List<String> fileUrls;
    private String problemStatement;  // PS
    private String solutionOverview;  // SO
    private String marketOpportunity; // MO
    private String competitiveAdvantage; // CA

    // 3 page
    @NotBlank private String licenseType; // EXCLUSIVE | NON_EXCLUSIVE

    //4 page
    @PositiveOrZero
    private Double reservePrice;
    @NotNull private String auctionDuration;
    private Double startingBid = 0.0;
}
