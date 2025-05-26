package com.kaankarahan.restiopha.backend.Application.Controller;

import com.kaankarahan.restiopha.backend.Application.Domain.Monster;
import com.kaankarahan.restiopha.backend.Application.Service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monsters")
public class MonsterController {
    private final MonsterService monsterService;

    @Autowired
    public MonsterController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }

    @GetMapping("/getAllMonsters")
    public List<Monster> getAllMonsters() {
        return monsterService.getAllMonsters();
    }

    @GetMapping("/{monsterId}")
    public Monster getMonsterById(@PathVariable Long monsterId) {
        return monsterService.getMonsterById(monsterId);
    }

    @PostMapping("/createMonster")
    @ResponseStatus(HttpStatus.CREATED)
    public Monster createMonster(@RequestBody Monster monster) {
        return monsterService.saveMonster(monster);
    }

    @DeleteMapping("/{monsterId}")
    public void deleteMonster(@PathVariable Long monsterId) {
        monsterService.deleteMonster(monsterId);
    }
    @GetMapping("/findAllAlive")
    public List<Monster> findAllAlive() {
        return monsterService.getAllMonsters();
    }
}