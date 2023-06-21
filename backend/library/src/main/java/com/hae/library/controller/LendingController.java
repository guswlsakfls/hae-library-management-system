package com.hae.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class LendingController {
    @PostMapping(value = "/v1/lending")
    public String LendingBook() {
        return "BookInfoController";
    }

    @PutMapping(value = "/v1/lending/{lendingId}")
    public String returningBook() {
        return "BookInfoController";
    }

    @GetMapping(value = "/v1/lending/{lendingId}/history")
    public String getOneLendingHistory() {
        return "BookInfoController";
    }

    @GetMapping(value = "/v1/lending/history")
    public String getAllLendingHistory() {
        return "BookInfoController";
    }

    @PutMapping(value = "/v1/lending/{lendingId}/renew")
    public String updateRenew() {
        return "BookInfoController";
    }

    @DeleteMapping(value = "/v1/lending/{lendingId}")
    public String deleteBookInfoByBookInfoId() {
        return "BookInfoController";
    }
}
