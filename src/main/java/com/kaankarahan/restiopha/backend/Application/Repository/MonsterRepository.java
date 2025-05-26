package com.kaankarahan.restiopha.backend.Application.Repository;

import com.kaankarahan.restiopha.backend.Application.Domain.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    @Query("SELECT m FROM Monster m WHERE m.hp > 0")
    List<Monster> findAllAlive();
}
