package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ClientEvents {
    private static long startupMessageEndTime = 0;
    private static boolean startupMessageShown = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        com.mrkun.bypass.keybind.KeyBindings.handleTick();
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
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
