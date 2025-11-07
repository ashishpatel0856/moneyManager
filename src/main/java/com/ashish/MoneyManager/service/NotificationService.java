package com.ashish.MoneyManager.service;

import com.ashish.MoneyManager.dto.ExpenseDto;
import com.ashish.MoneyManager.entity.ProfileEntity;
import com.ashish.MoneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.fronted.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone ="IST" )//10pm
    public void sendDailyIncomeExpenseReminder(){
        log.info("job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            String body="Hi " + profile.getFullName()+",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
                    + "<a href="+frontendUrl+" style='display:inline-black;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Manager<a>"
                    + "<br><br>Best regards,<br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(),"Daily reminder: Add your Income and Expense",body);

        }
        log.info("job finished: sendDailyIncomeExpenseReminder()");

    }
    @Scheduled(cron = "0 0 23 * * *",zone ="IST" )
    public void sendDailyExpenseSummary(){
        log.info("job started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
        List<ExpenseDto> todaysExpenses= expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
        if(!todaysExpenses.isEmpty()){
            StringBuilder table = new StringBuilder();
            table.append("<table style='border-collapse:collapse; width:100%;'>");
        }

        }
    }
}
