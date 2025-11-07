package com.ashish.MoneyManager.service;

import com.ashish.MoneyManager.dto.ExpenseDto;
import com.ashish.MoneyManager.dto.IncomeDto;
import com.ashish.MoneyManager.entity.CategoryEntity;
import com.ashish.MoneyManager.entity.ExpenseEntity;
import com.ashish.MoneyManager.entity.IncomeEntity;
import com.ashish.MoneyManager.entity.ProfileEntity;
import com.ashish.MoneyManager.repository.CategoryRepository;
import com.ashish.MoneyManager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final AppUserDetailsService userDetailsService;


// add new income
    public IncomeDto addIncome(IncomeDto dto) {
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        CategoryEntity category= categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Income not found"));
        IncomeEntity newExpense= toEntity(dto, profile, category);
        newExpense=incomeRepository.save(newExpense);
        return toDto(newExpense);

    }

// retrieves all INCOME for  current month , based on the start and end date
    public List<IncomeDto> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
        List<IncomeEntity> list= incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDto).toList();
    }


    // delete  incomes by expense by id
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        IncomeEntity entity=incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("income not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete income");

        }
        incomeRepository.delete(entity);
    }


    // getting latest five incomes for current user
    public List<IncomeDto> getLast5IncomesForCurrentUser(){
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
    }

    // get total income for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    private IncomeEntity toEntity(IncomeDto dto , ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(category)
                .profile(profile)
                .build();
    }


    private IncomeDto toDto(IncomeEntity entity){
        return IncomeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
