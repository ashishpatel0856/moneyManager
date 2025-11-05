package com.ashish.MoneyManager.service;

import com.ashish.MoneyManager.dto.AuthDto;
import com.ashish.MoneyManager.dto.ProfileDto;
import com.ashish.MoneyManager.entity.ProfileEntity;
import com.ashish.MoneyManager.repository.ProfileRepository;
import com.ashish.MoneyManager.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public ProfileDto registerProfile(ProfileDto profileDto) {

        ProfileEntity newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());

//        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword())); // hide password
        newProfile = profileRepository.save(newProfile);

        // send activation email
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject ="Activate your Money Manager account";
        String body = "Click on the following link to activate your Money Manager account:"+activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, body);
        return toDto(newProfile);

    }

    public ProfileEntity toEntity(ProfileDto profileDto) {
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .Password(passwordEncoder.encode(profileDto.getPassword()))
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }

    public ProfileDto toDto(ProfileEntity profileEntity) {
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);

    }


    public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto) {
        try{
              authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

              // generate jwt token
            String token = jwtUtil.generateToken(authDto.getEmail());
            return Map.of(
                    "token",token,
                    "user", userDetailsService.getPublicProfile(authDto.getEmail()
            ));


        } catch (Exception e){
            throw new BadCredentialsException("Invalid username or password");

        }
    }



}
