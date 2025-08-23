package com.Univauction.repository;

import com.Univauction.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByWalletAddress(String walletAddress);
    Wallet findByWalletAddress(String walletAddress);
}
