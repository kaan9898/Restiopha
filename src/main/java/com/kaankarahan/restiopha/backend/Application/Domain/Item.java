package com.kaankarahan.restiopha.backend.Application.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "item")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private int value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ItemType type;
    @Column(name = "attack_bonus")
    private Integer attackBonus;
    @Column(name = "speed_bonus")
    private Double speedBonus;

    public Object getId() {
        return itemId;
    }

    public enum ItemType {
        SWORD,
        SHOES
    }

    public static final Item RUSTY_BLADE = Item.builder()
            .name("Rusty Blade").value(50).type(ItemType.SWORD).attackBonus(1).build();
    public static final Item KNIGHTS_EDGE = Item.builder()
            .name("Knight's Edge").value(100).type(ItemType.SWORD).attackBonus(2).build();
    public static final Item CRYSTAL_SWORD = Item.builder()
            .name("Crystal Sword").value(200).type(ItemType.SWORD).attackBonus(3).build();

    public static final Item WORN_SHOES = Item.builder()
            .name("Worn Shoes").value(30).type(ItemType.SHOES).speedBonus(0.5).build();
    public static final Item LEATHER_BOOTS = Item.builder()
            .name("Leather Boots").value(60).type(ItemType.SHOES).speedBonus(1.0).build();
    public static final Item PHANTOM_STEPS = Item.builder()
            .name("Phantom Steps").value(120).type(ItemType.SHOES).speedBonus(2.0).build();

    public boolean isSword() {
        return type == ItemType.SWORD;
    }

    public boolean isShoes() {
        return type == ItemType.SHOES;
    }
}
