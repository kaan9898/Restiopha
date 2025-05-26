package com.kaankarahan.restiopha.backend.Application.Service;

import com.kaankarahan.restiopha.backend.Application.Domain.Monster;
import com.kaankarahan.restiopha.backend.Application.Repository.MonsterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonsterService {

    private final MonsterRepository monsterRepository;

    @Autowired
    public MonsterService(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    @Transactional
    public List<Monster> getAllMonsters() {
        return monsterRepository.findAll();
    }

    @Transactional
    public Monster getMonsterById(Long monsterId) {
        return monsterRepository.findById(monsterId)
                .orElseThrow(() -> new RuntimeException("Monster not found"));
    }

    @Transactional
    public Monster saveMonster(Monster monster) {
        return monsterRepository.save(monster);
    }

    @Transactional
    public void deleteMonster(Long monsterId) {
        monsterRepository.deleteById(monsterId);
    }
}
