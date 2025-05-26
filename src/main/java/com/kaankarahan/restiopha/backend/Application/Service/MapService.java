package com.kaankarahan.restiopha.backend.Application.Service;

import com.kaankarahan.restiopha.backend.Application.Repository.ItemRepository;
import com.kaankarahan.restiopha.backend.Application.Repository.MonsterRepository;
import com.kaankarahan.restiopha.backend.Application.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MapService {
    private static final int MAP_SIZE = 20;
    private final char[][] map = new char[MAP_SIZE][MAP_SIZE];
    private final PlayerRepository playerRepository;
    private final MonsterRepository monsterRepository;

    @Autowired
    public MapService(PlayerRepository playerRepository, MonsterRepository monsterRepository) {
        this.playerRepository = playerRepository;
        this.monsterRepository = monsterRepository;
    }

    @Scheduled(fixedRate = 1000)
    public void updateMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            Arrays.fill(map[i], '.');
        }
        map[5][5] = 'X';
        map[5][6] = 'X';
        playerRepository.findAll().forEach(p -> {
            map[p.getY()][p.getX()] = 'P';
        });
        monsterRepository.findAll().forEach(m -> {
            map[m.getY()][m.getX()] = 'M';
        });
    }

    public char[][] getCurrentMap() {
        return map;
    }

    public boolean isPositionWalkable(int x, int y) {
        return x >= 0 && x < MAP_SIZE &&
                y >= 0 && y < MAP_SIZE &&
                map[y][x] != 'X';
    }
}
