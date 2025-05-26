package com.kaankarahan.restiopha.backend.Application.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "player")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;
    @Column(name = "name")
    private String name;
    @Column(name = "hp")
    private int hp = 100;
    @Column(name = "base_attack")
    private int baseAttack = 10;
    @Column(name = "x")
    private int x = 0;
    @Column(name = "y")
    private int y = 0;
    @Column(name = "coins")
    private int coins = 0;
    @Column(name = "last_moved")
    private long lastMoved;
    @Column(name = "last_attacked")
    private long lastAttacked;
    @Column(name = "level")
    private int level = 1;
    @OneToOne
    @JoinColumn(name = "equipped_weapon_id")
    private Item equippedWeapon;

    @OneToOne
    @JoinColumn(name = "equipped_footwear_id")
    private Item equippedFootwear;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "player_inventory",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> inventory = new ArrayList<>();

}
