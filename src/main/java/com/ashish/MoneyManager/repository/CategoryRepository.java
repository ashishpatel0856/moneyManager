package com.ashish.MoneyManager.repository;

import com.ashish.MoneyManager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

//    select *from categories where profile_id =?1
    List<CategoryEntity> findByProfileId(Long profileId);

//    select * from categories where id =?1 and profile_id = ?2
    Optional<CategoryEntity> findByIdAndProfileId(Long id , Long profileId);

}
