package com.hae.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class LendingController {
    @PostMapping(value = "/lending")
    public String LendingBook() {
        return "BookInfoController";
    }

    @PutMapping(value = "/lending/{lendingId}")
    public String returningBook() {
        return "BookInfoController";
    }

    @GetMapping(value = "/lending/{lendingId}/history")
    public String getOneLendingHistory() {
        return "BookInfoController";
    }

    @GetMapping(value = "/lending/history")
    public String getAllLendingHistory() {
        return "BookInfoController";
    }

    @PutMapping(value = "/lending/{lendingId}/renew")
    public String updateRenew() {
        return "BookInfoController";
    }

    @DeleteMapping(value = "/lending/{lendingId}")
    public String deleteBookInfoByBookInfoId() {
        return "BookInfoController";
    }
}
