package com.kaankarahan.restiopha.backend.Application.Service;

import com.kaankarahan.restiopha.backend.Application.Domain.Item;
import com.kaankarahan.restiopha.backend.Application.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllSwords() {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getType() == Item.ItemType.SWORD)
                .collect(Collectors.toList());
    }

    public List<Item> getAllShoes() {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getType() == Item.ItemType.SHOES)
                .collect(Collectors.toList());
    }
}
