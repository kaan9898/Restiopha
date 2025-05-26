package com.kaankarahan.restiopha.backend.Application.Repository;

import com.kaankarahan.restiopha.backend.Application.Domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
