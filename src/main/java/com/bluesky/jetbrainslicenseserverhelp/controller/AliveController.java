package com.bluesky.jetbrainslicenseserverhelp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j(topic = "存活检查")
@RestController
@RequestMapping("/alive")
public class AliveController {
    @GetMapping()
    public ResponseEntity<Void> alive(
        @RequestParam(value = "ts", required = false) LocalDateTime ts,
        @RequestParam(value = "from", required = false) String from) {
        log.info("alive 入参校验: ts: {}, from: {}", ts, from);
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.toString();
        if (ObjectUtils.isEmpty(ts) || ObjectUtils.isEmpty(from)) {
            log.error("入参不能有空值: ts: {}, from: {}", ts, from);
            return ResponseEntity.badRequest()
                .header("Time", nowString)
                .build();
        }
        Duration diff = Duration.between(ts, now);
        if (diff.abs().toMillis() > 5 * 1000) {
            log.error("ts 超限: now:{}, ts:{}, diff: {}", now, ts, diff.toMillis());
            return ResponseEntity.badRequest()
                .header("Time", nowString)
                .build();
        }

        log.info("now: {}, ts: {}, diff: {}, from: {}", nowString, ts, diff.toMillis(), from);
        return ResponseEntity.noContent()
            .header("Time", nowString)
            .build();
    }
}