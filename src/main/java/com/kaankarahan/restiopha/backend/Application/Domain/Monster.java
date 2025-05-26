package com.kaankarahan.restiopha.backend.Application.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "monster")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_id")
    private Long monsterId;
    @Column(name = "name")
    private String name;
    @Column(name = "hp")
    private int hp;
    @Column(name = "attack_power")
    private int attackPower;
    @Column(name = "x")
    private int x;
    @Column(name = "y")
    private int y;
}
