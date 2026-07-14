package com.mrkun.bypass.keybind;

import com.mrkun.bypass.client.BypassMessage;
import com.mrkun.bypass.config.BypassConfig;
import com.mrkun.bypass.gui.BypassMenuScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static KeyBinding openMenuKey;
    private static KeyBinding flyKey;
    private static KeyBinding killAuraKey;
    private static KeyBinding speedKey;
    private static KeyBinding xrayKey;
    private static KeyBinding autoJumpKey;
    private static KeyBinding noFallKey;
    private static KeyBinding sprintKey;
    private static KeyBinding fullbrightKey;
    private static KeyBinding aimBotKey;
    private static KeyBinding espKey;
    private static KeyBinding antiCheatKey;

    public static void register() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.mrkunbypass"
        ));

        flyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.fly",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.mrkunbypass"
        ));

        killAuraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.killaura",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.mrkunbypass"
        ));

        speedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.speed",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.mrkunbypass"
        ));

        xrayKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.xray",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.mrkunbypass"
        ));

        autoJumpKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.autojump",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.mrkunbypass"
        ));

        noFallKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.nofall",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.mrkunbypass"
        ));

        sprintKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.sprint",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.mrkunbypass"
        ));

        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.fullbright",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.mrkunbypass"
        ));

        aimBotKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.aimbot",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_A,
                "category.mrkunbypass"
        ));

        espKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.esp",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "category.mrkunbypass"
        ));

        antiCheatKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mrkunbypass.anticheat",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.mrkunbypass"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKeyPresses();
        });
    }

    private static void handleKeyPresses() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        while (openMenuKey.wasPressed()) {
            client.setScreen(new BypassMenuScreen());
        }

        while (flyKey.wasPressed()) {
            toggleModule("Fly");
        }

        while (killAuraKey.wasPressed()) {
            toggleModule("KillAura");
        }

        while (speedKey.wasPressed()) {
            toggleModule("Speed");
        }

        while (xrayKey.wasPressed()) {
            toggleModule("Xray");
        }

        while (autoJumpKey.wasPressed()) {
            toggleModule("AutoJump");
        }

        while (noFallKey.wasPressed()) {
            toggleModule("NoFall");
        }

        while (sprintKey.wasPressed()) {
            toggleModule("Sprint");
        }

        while (fullbrightKey.wasPressed()) {
            toggleModule("Fullbright");
        }

        while (aimBotKey.wasPressed()) {
            toggleModule("AimBot");
        }

        while (espKey.wasPressed()) {
            toggleModule("ESP");
        }

        while (antiCheatKey.wasPressed()) {
            toggleModule("AntiCheat");
        }
    }

    private static void toggleModule(String moduleName) {
        BypassConfig.toggleModule(moduleName);
        boolean enabled = BypassConfig.isModuleEnabled(moduleName);
        BypassMessage.sendModuleToggleMessage(moduleName, enabled);
    }
}