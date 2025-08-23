package com.Univauction.controller;

import com.Univauction.dto.IdeaAuctionCreateRequest;
import com.Univauction.dto.IdeaAuctionCreateResponse;
import com.Univauction.service.IdeaAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class IdeaAuctionController {

    private final IdeaAuctionService ideaAuctionService;

    @PostMapping("/ideas-with-auction")
    public ResponseEntity<IdeaAuctionCreateResponse> createIdeaWithAuction(
            @Valid @RequestBody IdeaAuctionCreateRequest req
    ) {
        IdeaAuctionCreateResponse res = ideaAuctionService.createIdeaWithAuction(req);
        return ResponseEntity.ok(res);
    }
}
