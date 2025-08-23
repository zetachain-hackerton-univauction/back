package com.Univauction.controller;

import com.Univauction.dto.WalletRequestDto;
import com.Univauction.dto.WalletResponseDto;
import com.Univauction.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/enroll")
    public ResponseEntity<WalletResponseDto> registerWallet(
            @PathVariable Integer userId,
            @RequestBody WalletRequestDto requestDto) {
        WalletResponseDto response = walletService.registerWallet(userId, requestDto);
        return ResponseEntity.ok(response);
    }
}