package com.mrkun.bypass;

import com.mrkun.bypass.config.BypassConfig;
import com.mrkun.bypass.gui.WelcomeScreen;
import com.mrkun.bypass.keybind.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MrkunBypassClient implements ClientModInitializer {
    public static final String MOD_ID = "mrkunbypass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static long startupMessageEndTime = 0;
    private static boolean startupMessageShown = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mrkun Bypass loaded - troll client-side mod started!");

        BypassConfig.load();
        KeyBindings.register();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player == null) return;
            if (BypassConfig.isFirstLaunch()) {
                client.execute(() -> {
                    if (client.currentScreen == null) {
                        client.setScreen(new WelcomeScreen(null));
                    }
                });
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.currentScreen != null) return;
            if (!BypassConfig.isShowStartupNotification()) return;
            if (startupMessageShown) return;

            if (startupMessageEndTime == 0) {
                startupMessageEndTime = System.currentTimeMillis() + 5000;
            }

            if (System.currentTimeMillis() > startupMessageEndTime) {
                startupMessageShown = true;
                return;
            }

            Text message = Text.translatable("gui.mrkunbypass.notification.started")
                    .formatted(Formatting.GOLD, Formatting.BOLD);

            context.drawTextWithShadow(client.textRenderer, message, 10, 10, 0xFFFFFF);
        });
    }
}