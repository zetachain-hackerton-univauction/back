package com.Univauction.repository;

import com.Univauction.domain.User;
import com.Univauction.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByWalletAddress(String walletAddress);
    Wallet findByWalletAddress(String walletAddress);
    Optional<Wallet> findByUser(User user);
    Optional<Wallet> findTopByUserOrderByWalletIdAsc(User user);
}
