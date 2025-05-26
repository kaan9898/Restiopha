package com.kaankarahan.restiopha.backend.Application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GameExceptions {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class PlayerNotFoundException extends RuntimeException {
        public PlayerNotFoundException(Long playerId) {
            super("Player not found with id: " + playerId);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class MonsterNotFoundException extends RuntimeException {
        public MonsterNotFoundException(Long monsterId) {
            super("Monster not found with id: " + monsterId);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(Long itemId) {
            super("Item not found with id: " + itemId);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CooldownException extends RuntimeException {
        public CooldownException(String message, long remaining) {
            super("Exception: " + message + ": " + remaining);
        }
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CombatException extends RuntimeException {
        public CombatException(String message) {
            super("Exception: " + message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class NotEnoughCoinsException extends RuntimeException {
        public NotEnoughCoinsException(int cost) {
            super("Exception: " + cost);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidItemTypeException extends RuntimeException {
        public InvalidItemTypeException(String message) {
            super("Exception: " + message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidMovementException extends RuntimeException {
        public InvalidMovementException(String message) {
            super("Exception: " + message);
        }
    }
}
