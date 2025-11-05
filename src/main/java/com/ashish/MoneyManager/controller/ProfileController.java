package com.ashish.MoneyManager.controller;

import com.ashish.MoneyManager.dto.AuthDto;
import com.ashish.MoneyManager.dto.ProfileDto;
import com.ashish.MoneyManager.service.AppUserDetailsService;
import com.ashish.MoneyManager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final AppUserDetailsService appUserDetailsService;


    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto) {
        ProfileDto profile = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivated = profileService.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.status(HttpStatus.OK).body("Profile activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already  used");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String ,Object>> login( @RequestBody AuthDto authDto) {
      try{
          if(!appUserDetailsService.isAccountActive(authDto.getEmail())){
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is not active. Please activate your account first"));
          }
        Map<String,Object> response=  profileService.authenticateAndGenerateToken(authDto);
          return ResponseEntity.status(HttpStatus.OK).body(response);
      }catch (Exception e){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
      }
    }


}
