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
    // ---- 扩展的 10 个「假开挂」模块热键 ----
    private static KeyMapping reachKey;
    private static KeyMapping stepKey;
    private static KeyMapping jetpackKey;
    private static KeyMapping tracerKey;
    private static KeyMapping triggerBotKey;
    private static KeyMapping scaffoldKey;
    private static KeyMapping velocityKey;
    private static KeyMapping noClipKey;
    private static KeyMapping fastPlaceKey;
    private static KeyMapping hitBoxKey;

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
        // 扩展模块热键
        reachKey = make("key.mrkunbypass.reach", GLFW.GLFW_KEY_G);
        event.register(reachKey);
        stepKey = make("key.mrkunbypass.step", GLFW.GLFW_KEY_T);
        event.register(stepKey);
        jetpackKey = make("key.mrkunbypass.jetpack", GLFW.GLFW_KEY_Y);
        event.register(jetpackKey);
        tracerKey = make("key.mrkunbypass.tracer", GLFW.GLFW_KEY_U);
        event.register(tracerKey);
        triggerBotKey = make("key.mrkunbypass.triggerbot", GLFW.GLFW_KEY_I);
        event.register(triggerBotKey);
        scaffoldKey = make("key.mrkunbypass.scaffold", GLFW.GLFW_KEY_O);
        event.register(scaffoldKey);
        velocityKey = make("key.mrkunbypass.velocity", GLFW.GLFW_KEY_P);
        event.register(velocityKey);
        noClipKey = make("key.mrkunbypass.noclip", GLFW.GLFW_KEY_L);
        event.register(noClipKey);
        fastPlaceKey = make("key.mrkunbypass.fastplace", GLFW.GLFW_KEY_M);
        event.register(fastPlaceKey);
        hitBoxKey = make("key.mrkunbypass.hitbox", GLFW.GLFW_KEY_R);
        event.register(hitBoxKey);
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
        // 扩展模块
        if (reachKey.consumeClick()) toggle("Reach");
        if (stepKey.consumeClick()) toggle("Step");
        if (jetpackKey.consumeClick()) toggle("Jetpack");
        if (tracerKey.consumeClick()) toggle("Tracer");
        if (triggerBotKey.consumeClick()) toggle("TriggerBot");
        if (scaffoldKey.consumeClick()) toggle("Scaffold");
        if (velocityKey.consumeClick()) toggle("Velocity");
        if (noClipKey.consumeClick()) toggle("NoClip");
        if (fastPlaceKey.consumeClick()) toggle("FastPlace");
        if (hitBoxKey.consumeClick()) toggle("HitBox");
    }

    private static void toggle(String moduleName) {
        BypassConfig.toggleModule(moduleName);
        boolean enabled = BypassConfig.isModuleEnabled(moduleName);
        BypassMessage.sendModuleToggleMessage(moduleName, enabled);
    }
}
