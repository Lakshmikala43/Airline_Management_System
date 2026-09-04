package com.airline.service;

import com.airline.entity.SystemSetting;
import com.airline.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {

    private final SystemSettingRepository settingsRepository;

    @Transactional(readOnly = true)
    public List<SystemSetting> getAllSettings() {
        return settingsRepository.findAll();
    }

    @Transactional
    public SystemSetting updateSetting(String key, String value) {
        SystemSetting setting = settingsRepository.findBySettingKey(key)
            .orElseGet(() -> SystemSetting.builder().settingKey(key).settingGroup("GENERAL").build());

        setting.setSettingValue(value);
        setting.setUpdatedAt(ZonedDateTime.now());
        return settingsRepository.save(setting);
    }
}
