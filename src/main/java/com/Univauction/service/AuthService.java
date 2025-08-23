package com.Univauction.service;

import com.Univauction.domain.User;
import com.Univauction.domain.Wallet;
import com.Univauction.repository.UserRepository;
import com.Univauction.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public Wallet loginWithWallet(String walletAddress) {
        // 기존 지갑 조회
        Wallet wallet = walletRepository.findByWalletAddress(walletAddress);
        if (wallet != null) return wallet;

        // 없으면 신규 유저 생성
        User newUser = new User();
        userRepository.save(newUser);

        // 신규 지갑 생성
        Wallet newWallet = new Wallet();
        newWallet.setUser(newUser);
        newWallet.setWalletAddress(walletAddress);
        newWallet.setTokenName("SOL"); // 기본 토큰
        newWallet.setBalance(0.0);
        walletRepository.save(newWallet);

        return newWallet;
    }
}
