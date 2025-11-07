package com.ashish.MoneyManager.controller;

import com.ashish.MoneyManager.dto.ExpenseDto;
import com.ashish.MoneyManager.dto.IncomeDto;
import com.ashish.MoneyManager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;
    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto Dto) {
        IncomeDto saved = incomeService.addIncome(Dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getCurrentMonthExpensesForCurrentUser(){
        List<IncomeDto> dto = incomeService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long incomeId){
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.noContent().build();
    }
}
