package com.mrkun.bypass.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BypassConfig {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("MrkunBypass");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FOLDER = "Mrkun_Bypass_config";
    private static final String CONFIG_FILE = "mrkun_bypass_config.json";

    private static final Map<String, Boolean> MODULE_STATES = new HashMap<>();
    private static boolean showStartupNotification = true;
    private static boolean backgroundBlur = false;
    private static boolean firstLaunch = true;
    private static boolean showStatistics = true;
    private static boolean soundEffects = true;
    private static int totalToggles = 0;
    private static long sessionStartTime = 0;

    static {
        MODULE_STATES.put("Fly", false);
        MODULE_STATES.put("KillAura", false);
        MODULE_STATES.put("Speed", false);
        MODULE_STATES.put("Xray", false);
        MODULE_STATES.put("AutoJump", false);
        MODULE_STATES.put("NoFall", false);
        MODULE_STATES.put("Sprint", false);
        MODULE_STATES.put("Fullbright", false);
        MODULE_STATES.put("AimBot", false);
        MODULE_STATES.put("ESP", false);
        MODULE_STATES.put("AntiCheat", false);
    }

    private static Path configDir() {
        return FMLPaths.GAME_DIR.get().resolve(CONFIG_FOLDER);
    }

    private static Path configPath() {
        return configDir().resolve(CONFIG_FILE);
    }

    public static void load() {
        Path dir = configDir();
        Path path = configPath();
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
                LOGGER.info("Created config directory: {}", dir);
            }

            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        if (data.modules != null) {
                            for (Map.Entry<String, Boolean> entry : data.modules.entrySet()) {
                                if (MODULE_STATES.containsKey(entry.getKey())) {
                                    MODULE_STATES.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                        showStartupNotification = data.showStartupNotification;
                        backgroundBlur = data.backgroundBlur;
                        firstLaunch = data.firstLaunch;
                        if (data.showStatistics != null) showStatistics = data.showStatistics;
                        if (data.soundEffects != null) soundEffects = data.soundEffects;
                        if (data.totalToggles != null) totalToggles = data.totalToggles;
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load Mrkun Bypass config", e);
        }
        sessionStartTime = System.currentTimeMillis();
    }

    public static void save() {
        Path dir = configDir();
        Path path = configPath();
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            ConfigData data = new ConfigData();
            data.modules = new HashMap<>(MODULE_STATES);
            data.showStartupNotification = showStartupNotification;
            data.backgroundBlur = backgroundBlur;
            data.firstLaunch = firstLaunch;
            data.showStatistics = showStatistics;
            data.soundEffects = soundEffects;
            data.totalToggles = totalToggles;
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save Mrkun Bypass config", e);
        }
    }

    public static boolean isModuleEnabled(String moduleName) {
        return MODULE_STATES.getOrDefault(moduleName, false);
    }

    public static void toggleModule(String moduleName) {
        MODULE_STATES.put(moduleName, !isModuleEnabled(moduleName));
        totalToggles++;
        save();
    }

    public static void setModule(String moduleName, boolean enabled) {
        MODULE_STATES.put(moduleName, enabled);
        save();
    }

    public static Map<String, Boolean> getAllModules() {
        return new HashMap<>(MODULE_STATES);
    }

    public static String[] getModuleNames() {
        return MODULE_STATES.keySet().toArray(new String[0]);
    }

    public static boolean isShowStartupNotification() {
        return showStartupNotification;
    }

    public static void setShowStartupNotification(boolean value) {
        showStartupNotification = value;
        save();
    }

    public static boolean isBackgroundBlur() {
        return backgroundBlur;
    }

    public static void setBackgroundBlur(boolean value) {
        backgroundBlur = value;
        save();
    }

    public static boolean isFirstLaunch() {
        return firstLaunch;
    }

    public static void setFirstLaunch(boolean value) {
        firstLaunch = value;
        save();
    }

    public static boolean isShowStatistics() {
        return showStatistics;
    }

    public static void setShowStatistics(boolean value) {
        showStatistics = value;
        save();
    }

    public static boolean isSoundEffects() {
        return soundEffects;
    }

    public static void setSoundEffects(boolean value) {
        soundEffects = value;
        save();
    }

    public static int getTotalToggles() {
        return totalToggles;
    }

    public static int getActiveModuleCount() {
        int count = 0;
        for (Boolean enabled : MODULE_STATES.values()) {
            if (enabled) count++;
        }
        return count;
    }

    public static String getSessionTime() {
        long elapsed = System.currentTimeMillis() - sessionStartTime;
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / 60000) % 60;
        long hours = (elapsed / 3600000);
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    private static class ConfigData {
        Map<String, Boolean> modules;
        boolean showStartupNotification = true;
        boolean backgroundBlur = false;
        boolean firstLaunch = true;
        Boolean showStatistics = true;
        Boolean soundEffects = true;
        Integer totalToggles = 0;
    }
}
