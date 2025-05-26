package com.kaankarahan.restiopha.backend.Application.Controller;

import com.kaankarahan.restiopha.backend.Application.Domain.Item;
import com.kaankarahan.restiopha.backend.Application.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/swords")
    public List<Item> getAllSwords() {
        return itemService.getAllSwords();
    }

    @GetMapping("/shoes")
    public List<Item> getAllShoes() {
        return itemService.getAllShoes();
    }
}
