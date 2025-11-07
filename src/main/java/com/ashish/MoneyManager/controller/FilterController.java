package com.ashish.MoneyManager.controller;

import com.ashish.MoneyManager.dto.ExpenseDto;
import com.ashish.MoneyManager.dto.FilterDto;
import com.ashish.MoneyManager.dto.IncomeDto;
import com.ashish.MoneyManager.service.ExpenseService;
import com.ashish.MoneyManager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDto filterDto) {
        LocalDate startDate = filterDto.getStartDate() != null ? filterDto.getStartDate() : LocalDate.now().minusYears(50);
        LocalDate endDate = filterDto.getEndDate() !=null ? filterDto.getEndDate() : LocalDate.now();
        String keyword=filterDto.getKeyword()!=null ? filterDto.getKeyword() : "";


        String sortField = filterDto.getSortField();
        if (sortField == null || sortField.isBlank()) {
            sortField = "date"; // Default field to sort by (change if needed)
        }

        Sort.Direction direction = Sort.Direction.ASC; // default sort direction
        if ("desc".equalsIgnoreCase(filterDto.getSortOrder())) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortField);

        if("income".equals(filterDto.getType())){
           List<IncomeDto> incomeDto= incomeService.filterIncome(startDate,endDate,keyword,sort);
           return ResponseEntity.ok(incomeDto);

        } else if("expense".equals(filterDto.getType())){
            List<ExpenseDto> dto = expenseService.filterExpenses(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(dto);
        } else{
            return ResponseEntity.badRequest().body("Invalid type, Must be income or expense");
        }
    }
}
