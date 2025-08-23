package com.Univauction.service;

import com.Univauction.domain.User;
import com.Univauction.domain.Wallet;
import com.Univauction.dto.WalletRequestDto;
import com.Univauction.dto.WalletResponseDto;
import com.Univauction.repository.UserRepository;
import com.Univauction.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Transactional
    public WalletResponseDto registerWallet(Integer userId, WalletRequestDto requestDto) {
        // 이미 등록된 지갑인지 체크
        if (walletRepository.existsByWalletAddress(requestDto.getWalletAddress())) {
            throw new RuntimeException("이미 등록된 지갑 주소입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 엔티티 생성
        Wallet wallet = Wallet.builder()
                .user(user)
                .walletAddress(requestDto.getWalletAddress())
                .tokenName(requestDto.getTokenName())
                .balance(0.0)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);

        return new WalletResponseDto(savedWallet);
    }
}
