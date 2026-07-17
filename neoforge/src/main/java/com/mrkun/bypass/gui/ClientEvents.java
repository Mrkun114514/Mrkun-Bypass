package com.mrkun.bypass.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrkun.bypass.client.FakeEffects;
import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public class ClientEvents {
    private static long startupMessageEndTime = 0;
    private static boolean startupMessageShown = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        com.mrkun.bypass.keybind.KeyBindings.handleTick();
    }

    @SubscribeEvent
    public void onRenderLevel(RenderLevelStageEvent event) {
        // 捕获世界渲染的 view/projection 矩阵，供 FakeEffects 把实体投影到屏幕画锁定环
        FakeEffects.captureFrame(
                new Matrix4f(RenderSystem.getModelViewMatrix()),
                new Matrix4f(RenderSystem.getProjectionMatrix())
        );
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            FakeEffects.renderHud(event.getGuiGraphics());
        }
        if (mc.player == null || mc.screen != null) return;
        if (!BypassConfig.isShowStartupNotification()) return;
        if (startupMessageShown) return;

        if (startupMessageEndTime == 0) {
            startupMessageEndTime = System.currentTimeMillis() + 5000;
        }

        if (System.currentTimeMillis() > startupMessageEndTime) {
            startupMessageShown = true;
            return;
        }

        Component message = Component.translatable("gui.mrkunbypass.notification.started")
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);

        event.getGuiGraphics().drawString(mc.font, message, 10, 10, 0xFFFFFF);
    }

    @SubscribeEvent
    public void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft mc = Minecraft.getInstance();
        if (BypassConfig.isFirstLaunch() && mc.screen == null) {
            mc.setScreen(new WelcomeScreen(null));
        }
    }
}
