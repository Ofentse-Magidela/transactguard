package com.transactguard.transactguard.repo;

import com.transactguard.transactguard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,  Integer> {

}
