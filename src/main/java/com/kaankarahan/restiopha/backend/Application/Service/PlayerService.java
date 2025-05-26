package com.kaankarahan.restiopha.backend.Application.Service;

import com.kaankarahan.restiopha.backend.Application.Domain.*;
import com.kaankarahan.restiopha.backend.Application.Repository.MonsterRepository;
import com.kaankarahan.restiopha.backend.Application.Repository.PlayerRepository;
import com.kaankarahan.restiopha.backend.Application.Exception.GameExceptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final MapService mapService;
    private final MonsterRepository monsterRepository;
    private static final long MONSTER_SPAWN_INTERVAL = 30000;
    private static final long BASE_MOVEMENT_COOLDOWN = 500;
    private static final long ATTACK_COOLDOWN = 1000;

    @Autowired
    public PlayerService(PlayerRepository playerRepository,
                         MonsterRepository monsterRepository, MapService mapService) {
        this.playerRepository = playerRepository;
        this.monsterRepository = monsterRepository;
        this.mapService = mapService;
    }

    @Transactional
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Transactional
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new GameExceptions.PlayerNotFoundException(playerId));
    }

    @Transactional
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Scheduled(fixedRate = MONSTER_SPAWN_INTERVAL)
    @Transactional
    public void spawnMonsters() {
        Monster monster = Monster.builder()
                .name("M")
                .hp(30)
                .attackPower(5)
                .x((int) (Math.random() * 20))
                .y((int) (Math.random() * 20))
                .build();
        monsterRepository.save(monster);
    }

    @Transactional
    public ResponseEntity<String> movePlayer(Long playerId, String direction) {
        Player player = getPlayerById(playerId);
        long currentTime = System.currentTimeMillis();
        long cooldown = calculateMovementCooldown(player);
        if (currentTime - player.getLastMoved() < cooldown) {
            long remaining = cooldown - (currentTime - player.getLastMoved());
            throw new GameExceptions.CooldownException("Movement", remaining);
        }
        Position newPosition = calculateNewPosition(player, direction);
        if (!mapService.isPositionWalkable(newPosition.x(), newPosition.y())) {
            throw new GameExceptions.InvalidMovementException("Cannot move there!");
        }
        player.setX(newPosition.x());
        player.setY(newPosition.y());
        player.setLastMoved(currentTime);
        playerRepository.save(player);

        return ResponseEntity.ok(String.format(
                "Moved to (%d,%d). Next move in %dms",
                player.getX(), player.getY(), cooldown
        ));
    }

    private long calculateMovementCooldown(Player player) {
        double speedBonus = player.getEquippedFootwear() != null ?
                player.getEquippedFootwear().getSpeedBonus() : 0.0;
        return (long) (BASE_MOVEMENT_COOLDOWN / (1 + speedBonus));
    }

    @Transactional
    public ResponseEntity<String> attackMonster(Long playerId, Long monsterId) {
        Player player = getPlayerById(playerId);
        Monster monster = monsterRepository.findById(monsterId)
                .orElseThrow(() -> new GameExceptions.MonsterNotFoundException(monsterId));
        if (!isAdjacent(player, monster)) {
            throw new GameExceptions.CombatException("Monster too far away!");
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - player.getLastAttacked() < ATTACK_COOLDOWN) {
            long remaining = ATTACK_COOLDOWN - (currentTime - player.getLastAttacked());
            throw new GameExceptions.CooldownException("Attack", remaining);
        }
        int damage = calculateDamage(player);
        monster.setHp(monster.getHp() - damage);
        int counterDamage = monster.getAttackPower();
        player.setHp(player.getHp() - counterDamage);

        if (monster.getHp() <= 0) {
            handleMonsterDefeat(player, monster);
            return ResponseEntity.ok(String.format(
                    "Monster defeated! Dropped: %s (Damage: %d, HP: %d)",
                    generateLoot(),damage, player.getHp()
            ));
        } else {
            monsterRepository.save(monster);
            player.setLastAttacked(currentTime);
            playerRepository.save(player);
            return ResponseEntity.ok(String.format(
                    "Monster HP: %d (Damage dealt: %d), Player HP: %d (Damage taken: %d)",
                    monster.getHp(), damage, player.getHp(), counterDamage
            ));
        }
    }
    private boolean isAdjacent(Player player, Monster monster) {
        return Math.abs(player.getX() - monster.getX()) <= 1 &&
                Math.abs(player.getY() - monster.getY()) <= 1;
    }

    private void handleMonsterDefeat(Player player, Monster monster) {
        Item loot = generateLoot();
        player.getInventory().add(loot);
        monsterRepository.delete(monster);
        playerRepository.save(player);
    }

    @Transactional
    public ResponseEntity<String> levelUp(Long playerId) {
        Player player = getPlayerById(playerId);
        int cost = player.getLevel() * 100;

        if (player.getCoins() < cost) {
            throw new GameExceptions.NotEnoughCoinsException(cost);
        }
        player.setCoins(player.getCoins() - cost);
        player.setLevel(player.getLevel() + 1);
        player.setBaseAttack(player.getBaseAttack() + 2);
        player.setHp(player.getHp() + 10);
        playerRepository.save(player);
        return ResponseEntity.ok(String.format(
                "Level up! Now level %d (Attack: %d, HP: %d)",
                player.getLevel(), player.getBaseAttack(), player.getHp()
        ));
    }

    @Transactional
    public ResponseEntity<String> equipItem(Long playerId, Long itemId, Item.ItemType itemType) {
        Player player = getPlayerById(playerId);

        Item item = player.getInventory().stream()
                .filter(i -> i.getId().equals(itemId) && i.getType() == itemType)
                .findFirst()
                .orElseThrow(() -> new GameExceptions.ItemNotFoundException(itemId));

        switch (itemType) {
            case SWORD -> {
                if (!item.isSword()) throw new GameExceptions.InvalidItemTypeException("Item is not a sword");
                player.setEquippedWeapon(item);
            }
            case SHOES -> {
                if (!item.isShoes()) throw new GameExceptions.InvalidItemTypeException("Item is not shoes");
                player.setEquippedFootwear(item);
            }
        }

        playerRepository.save(player);
        return ResponseEntity.ok(itemType.name().toLowerCase() + " equipped successfully");
    }

    @Transactional
    public ResponseEntity<String> sellItem(Long playerId, Long itemId, Item.ItemType itemType) {

        Player player = getPlayerById(playerId);
        Item item = player.getInventory().stream()
                .filter(i -> i.getId().equals(itemId) && i.getType() == itemType)
                .findFirst()
                .orElseThrow(() -> new GameExceptions.ItemNotFoundException(itemId));

        player.getInventory().remove(item);
        player.setCoins(player.getCoins() + item.getValue());
        playerRepository.save(player);

        return ResponseEntity.ok(String.format(
                "Sold %s for %d coins. New balance: %d",
                item.getName(),
                item.getValue(),
                player.getCoins()
        ));
    }

    @Transactional
    public List<Item> getPlayerInventory(Long playerId) {
        return getPlayerById(playerId).getInventory();
    }

    private Position calculateNewPosition(Player player, String direction) {
        int x = player.getX();
        int y = player.getY();

        switch (direction.toUpperCase()) {
            case "UP" -> y--;
            case "DOWN" -> y++;
            case "LEFT" -> x--;
            case "RIGHT" -> x++;
            default -> throw new GameExceptions.InvalidMovementException("Invalid direction: " + direction);
        }

        return new Position(x, y);
    }

    private int calculateDamage(Player player) {
        return player.getBaseAttack() +
                (player.getEquippedWeapon() != null ?
                        player.getEquippedWeapon().getAttackBonus() : 0);
    }

    private Item generateLoot() {
        Random random = new Random();
        if (random.nextBoolean()) {
            return switch (random.nextInt(3)) {
                case 0 -> Item.RUSTY_BLADE;
                case 1 -> Item.KNIGHTS_EDGE;
                default -> Item.CRYSTAL_SWORD;
            };
        } else {
            return switch (random.nextInt(3)) {
                case 0 -> Item.WORN_SHOES;
                case 1 -> Item.LEATHER_BOOTS;
                default -> Item.PHANTOM_STEPS;
            };
        }
    }

    private record Position(int x, int y) {}
}