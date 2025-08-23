package com.Univauction.repository;

import com.Univauction.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findByWalletAddress(String walletAddress);

}
