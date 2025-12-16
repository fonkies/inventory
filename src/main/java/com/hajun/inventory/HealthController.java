package com.hajun.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
    @GetMapping("/boom/notfound")
    public String nf() {
        throw new NotFoundException("없음 테스트");
    }

    @GetMapping("/boom/conflict")
    public String cf() {
        throw new ConflictException("중복 테스트");
    }

}
