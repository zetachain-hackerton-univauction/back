package com.Univauction.repository;

import com.Univauction.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //일단 이따가염
}
