package com.example.blog.repository;

import com.example.blog.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    Optional<Settings> findBySettingKey(String key);
    List<Settings> findBySettingKeyIn(List<String> keys);
}
