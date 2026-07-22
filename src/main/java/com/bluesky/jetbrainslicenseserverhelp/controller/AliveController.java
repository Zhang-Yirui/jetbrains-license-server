package com.bluesky.jetbrainslicenseserverhelp.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "存活检查")
@RestController
@RequestMapping("/alive")
public class AliveController {
    @GetMapping()
    public Map<String, Object> alive() {
        log.info("存活检查");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "alive");
        return response;
    }
}