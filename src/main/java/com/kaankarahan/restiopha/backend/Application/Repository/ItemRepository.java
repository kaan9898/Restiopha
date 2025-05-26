package com.kaankarahan.restiopha.backend.Application.Repository;

import com.kaankarahan.restiopha.backend.Application.Domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
