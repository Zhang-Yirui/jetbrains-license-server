package com.bd3qif.LicenseServer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        @RequestParam(value = "verify_time", required = false, defaultValue = "true") Boolean verifyTime,
        @RequestParam(value = "ts", required = false, defaultValue = "1970-01-01T00:00:00") LocalDateTime ts,
        @RequestParam(value = "from", required = false, defaultValue = "unknown") String from) {
        log.info("alive 入参: skip_verify: {}, ts: {}, from: {}", verifyTime, ts, from);
        HttpStatusCode status = HttpStatus.NO_CONTENT;
        if (Boolean.TRUE.equals(verifyTime)) {
            LocalDateTime now = LocalDateTime.now();
            Duration diff = Duration.between(ts, now);
            if (diff.abs().toMillis() > 5 * 1000) {
                log.error("ts 超限: now:{}, ts:{}, diff: {}", now, ts, diff.toMillis());
                status = HttpStatus.BAD_REQUEST;
            } else {
                log.info("now: {}, ts: {}, diff: {}, from: {}", now, ts, diff.toMillis(), from);
            }
        }
        return ResponseEntity.status(status).header("Checker", from).build();
    }
}