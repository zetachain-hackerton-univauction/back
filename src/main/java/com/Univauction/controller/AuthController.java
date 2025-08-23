package com.Univauction.controller;

import com.Univauction.dto.WalletLoginRequest;
import com.Univauction.dto.WalletResponse;
import com.Univauction.domain.Wallet;
import com.Univauction.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public WalletResponse login(@RequestBody WalletLoginRequest request) {
        Wallet wallet = authService.loginWithWallet(request.getWalletAddress());
        return new WalletResponse(wallet.getWalletId(), wallet.getWalletAddress(), wallet.getTokenName(), wallet.getBalance());
    }
}