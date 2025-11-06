package com.ashish.MoneyManager.service;

import com.ashish.MoneyManager.dto.CategoryDto;
import com.ashish.MoneyManager.entity.CategoryEntity;
import com.ashish.MoneyManager.entity.ProfileEntity;
import com.ashish.MoneyManager.repository.CategoryRepository;
import com.ashish.MoneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AppUserDetailsService userDetailsService;
    private final ProfileRepository profileRepository;

    public CategoryDto saveCategory(CategoryDto categoryDto) {
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDto.getName(),profile.getId())){
            throw new RuntimeException("Category with this name already exists");
        }
        CategoryEntity newCategory =toEntity(categoryDto,profile);
        newCategory =categoryRepository.save(newCategory);
       return toDto(newCategory);
    }


//  get category by type for current user
    public List<CategoryDto> getAllCategoriesByTypeForCurrentUser  (String type) {
        ProfileEntity profileEntity = userDetailsService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type,profileEntity.getId());
        return entities
                .stream()
                .map(this::toDto)
                .toList();
    }

    // get allCategory for current user
    public List<CategoryDto> getAllCategoriesForCurrentUser(){
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        List<CategoryEntity> categoryEntities = categoryRepository.findByProfileId(profile.getId());
        return categoryEntities
                .stream()
                .map(this::toDto)
                .toList();
    }


    // update category by categoryId
    public CategoryDto updateCategory(Long categoryId,CategoryDto categoryDto) {
        ProfileEntity profile = userDetailsService.getCurrentProfile();
        CategoryEntity existingCategory= categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(() -> new RuntimeException("Category with this id does not exist"));
        existingCategory.setName(categoryDto.getName());
        existingCategory.setIcon(categoryDto.getIcon());
        existingCategory= categoryRepository.save(existingCategory);
        return toDto(existingCategory);
    }


    public CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profileEntity)
                .type(categoryDto.getType())
                .build();

    }
    public CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() !=null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }
}
