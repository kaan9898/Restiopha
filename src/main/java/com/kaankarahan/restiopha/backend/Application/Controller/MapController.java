package com.kaankarahan.restiopha.backend.Application.Controller;

import com.kaankarahan.restiopha.backend.Application.Service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/getMap")
    public char[][] getMap() {
        return mapService.getCurrentMap();
    }
}