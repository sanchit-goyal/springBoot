package com.example.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbatch.entity.UserEntiry;

@Repository
public interface UserRepository extends JpaRepository<UserEntiry, Long> {

}
