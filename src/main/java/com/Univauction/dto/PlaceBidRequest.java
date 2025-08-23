package com.Univauction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlaceBidRequest {
    private Integer userId;
    private String tokenSymbol;
    private Double bidAmount;
    private String comment;
}