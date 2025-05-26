package com.kaankarahan.restiopha.backend.Application.Controller;

import com.kaankarahan.restiopha.backend.Application.Domain.Item;
import com.kaankarahan.restiopha.backend.Application.Domain.Player;
import com.kaankarahan.restiopha.backend.Application.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/getAllPlayers")
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{playerId}")
    public Player getPlayerById(@PathVariable Long playerId) {
        return playerService.getPlayerById(playerId);
    }

    @PostMapping("/createPlayer")
    @ResponseStatus(HttpStatus.CREATED)
    public Player createPlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @PostMapping("/{playerId}/move")
    public ResponseEntity<String> movePlayer(@PathVariable Long playerId, @RequestParam String direction) {
        return playerService.movePlayer(playerId, direction);
    }

    @PostMapping("/{playerId}/attack/{monsterId}")
    public ResponseEntity<String> attackMonster(@PathVariable Long playerId, @PathVariable Long monsterId) {
        return playerService.attackMonster(playerId, monsterId);
    }

    @PostMapping("/{playerId}/equip")
    public ResponseEntity<String> equipItem(@PathVariable Long playerId, @RequestParam Long itemId, @RequestParam Item.ItemType itemType) {
        return playerService.equipItem(playerId, itemId, itemType);
    }

    @PostMapping("/{playerId}/sell")
    public ResponseEntity<String> sellItem(@PathVariable Long playerId, @RequestParam Long itemId, @RequestParam Item.ItemType itemType) {
        return playerService.sellItem(playerId, itemId, itemType);
    }

    @GetMapping("/{playerId}/inventory")
    public List<Item> getPlayerInventory(@PathVariable Long playerId) {
        return playerService.getPlayerInventory(playerId);
    }

    @PostMapping("/{playerId}/level")
    public ResponseEntity<String> levelUp(@PathVariable Long playerId) {
        return playerService.levelUp(playerId);
    }
}