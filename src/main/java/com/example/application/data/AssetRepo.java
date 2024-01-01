package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AssetRepo extends JpaRepository<AssetEntity, Integer> {
    List<AssetEntity> findByLocalDateTimeBetween(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}
