package com.suhasm.ecommerce.controller;

import com.suhasm.ecommerce.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis")
public class RedisTestController {

    private final RedisTestService redisTestService;

    @PostMapping("/test")
    public String setValue(
            @RequestParam String key,
            @RequestParam String value) {

        redisTestService.setValue(key, value);
        return "Saved";
    }

    @GetMapping("/test")
    public String getValue(@RequestParam String key) {
        return redisTestService.getValue(key);
    }
}
