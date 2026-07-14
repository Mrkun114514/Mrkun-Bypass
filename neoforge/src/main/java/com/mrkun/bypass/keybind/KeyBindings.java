package com.mrkun.bypass.keybind;

import com.mrkun.bypass.client.BypassMessage;
import com.mrkun.bypass.config.BypassConfig;
import com.mrkun.bypass.gui.BypassMenuScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyMapping openMenuKey;
    private static KeyMapping flyKey;
    private static KeyMapping killAuraKey;
    private static KeyMapping speedKey;
    private static KeyMapping xrayKey;
    private static KeyMapping autoJumpKey;
    private static KeyMapping noFallKey;
    private static KeyMapping sprintKey;
    private static KeyMapping fullbrightKey;
    private static KeyMapping aimBotKey;
    private static KeyMapping espKey;
    private static KeyMapping antiCheatKey;

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        openMenuKey = make("key.mrkunbypass.open_menu", GLFW.GLFW_KEY_H);
        event.register(openMenuKey);
        flyKey = make("key.mrkunbypass.fly", GLFW.GLFW_KEY_F);
        event.register(flyKey);
        killAuraKey = make("key.mrkunbypass.killaura", GLFW.GLFW_KEY_K);
        event.register(killAuraKey);
        speedKey = make("key.mrkunbypass.speed", GLFW.GLFW_KEY_V);
        event.register(speedKey);
        xrayKey = make("key.mrkunbypass.xray", GLFW.GLFW_KEY_X);
        event.register(xrayKey);
        autoJumpKey = make("key.mrkunbypass.autojump", GLFW.GLFW_KEY_J);
        event.register(autoJumpKey);
        noFallKey = make("key.mrkunbypass.nofall", GLFW.GLFW_KEY_N);
        event.register(noFallKey);
        sprintKey = make("key.mrkunbypass.sprint", GLFW.GLFW_KEY_Z);
        event.register(sprintKey);
        fullbrightKey = make("key.mrkunbypass.fullbright", GLFW.GLFW_KEY_B);
        event.register(fullbrightKey);
        aimBotKey = make("key.mrkunbypass.aimbot", GLFW.GLFW_KEY_A);
        event.register(aimBotKey);
        espKey = make("key.mrkunbypass.esp", GLFW.GLFW_KEY_E);
        event.register(espKey);
        antiCheatKey = make("key.mrkunbypass.anticheat", GLFW.GLFW_KEY_C);
        event.register(antiCheatKey);
    }

    private static KeyMapping make(String name, int key) {
        return new KeyMapping(name, key, "category.mrkunbypass");
    }

    public static void handleTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (openMenuKey.consumeClick()) {
            mc.setScreen(new BypassMenuScreen());
        }
        if (flyKey.consumeClick()) toggle("Fly");
        if (killAuraKey.consumeClick()) toggle("KillAura");
        if (speedKey.consumeClick()) toggle("Speed");
        if (xrayKey.consumeClick()) toggle("Xray");
        if (autoJumpKey.consumeClick()) toggle("AutoJump");
        if (noFallKey.consumeClick()) toggle("NoFall");
        if (sprintKey.consumeClick()) toggle("Sprint");
        if (fullbrightKey.consumeClick()) toggle("Fullbright");
        if (aimBotKey.consumeClick()) toggle("AimBot");
        if (espKey.consumeClick()) toggle("ESP");
        if (antiCheatKey.consumeClick()) toggle("AntiCheat");
    }

    private static void toggle(String moduleName) {
        BypassConfig.toggleModule(moduleName);
        boolean enabled = BypassConfig.isModuleEnabled(moduleName);
        BypassMessage.sendModuleToggleMessage(moduleName, enabled);
    }
}
