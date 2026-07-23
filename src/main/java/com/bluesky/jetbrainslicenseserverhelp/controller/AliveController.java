package com.bluesky.jetbrainslicenseserverhelp.controller;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "存活检查")
@RestController
@RequestMapping("/alive")
public class AliveController {
    @GetMapping()
    public ResponseEntity<Void> alive(
            @RequestParam(value = "ts", required = false) LocalDateTime ts,
            @RequestParam(value = "from", required = false) String from) {
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.toString();
        
        if (ObjectUtils.isEmpty(ts) || now.isAfter(ts)) {
            return ResponseEntity.badRequest()
                    .header("Time", nowString)
                    .build();
        }

        if (ObjectUtils.isEmpty(from)) {
            from = "Unknown";
        }

        log.info("检查时间: {}, ts: {}, from: {}", nowString, ts.toString(), from);
        return ResponseEntity.noContent()
                .header("Time", nowString)
                .build();
    }
}