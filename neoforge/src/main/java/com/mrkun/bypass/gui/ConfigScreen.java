package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("gui.mrkunbypass.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 - 60;

        Button startupButton = Button.builder(
                        getStartupText(),
                        btn -> {
                            BypassConfig.setShowStartupNotification(!BypassConfig.isShowStartupNotification());
                            btn.setMessage(getStartupText());
                        })
                .bounds(x, y, buttonWidth, 20)
                .build();
        this.addRenderableWidget(startupButton);

        y += 25;

        Button blurButton = Button.builder(
                        getBlurText(),
                        btn -> {
                            BypassConfig.setBackgroundBlur(!BypassConfig.isBackgroundBlur());
                            btn.setMessage(getBlurText());
                        })
                .bounds(x, y, buttonWidth, 20)
                .build();
        this.addRenderableWidget(blurButton);

        y += 25;

        Button statisticsButton = Button.builder(
                        getStatisticsText(),
                        btn -> {
                            BypassConfig.setShowStatistics(!BypassConfig.isShowStatistics());
                            btn.setMessage(getStatisticsText());
                        })
                .bounds(x, y, buttonWidth, 20)
                .build();
        this.addRenderableWidget(statisticsButton);

        y += 25;

        Button soundButton = Button.builder(
                        getSoundText(),
                        btn -> {
                            BypassConfig.setSoundEffects(!BypassConfig.isSoundEffects());
                            btn.setMessage(getSoundText());
                        })
                .bounds(x, y, buttonWidth, 20)
                .build();
        this.addRenderableWidget(soundButton);

        y += 40;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.mrkunbypass.config.done"), btn -> this.close())
                .bounds(x, y, buttonWidth, 20)
                .build());
    }

    private Component getStartupText() {
        boolean enabled = BypassConfig.isShowStartupNotification();
        return Component.translatable("gui.mrkunbypass.config.startup_notification")
                .append(Component.literal(": ")
                        .append(Component.literal(enabled ? "ON" : "OFF")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));
    }

    private Component getBlurText() {
        boolean enabled = BypassConfig.isBackgroundBlur();
        return Component.translatable("gui.mrkunbypass.config.background_blur")
                .append(Component.literal(": ")
                        .append(Component.literal(enabled ? "ON" : "OFF")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));
    }

    private Component getStatisticsText() {
        boolean enabled = BypassConfig.isShowStatistics();
        return Component.translatable("gui.mrkunbypass.config.statistics")
                .append(Component.literal(": ")
                        .append(Component.literal(enabled ? "ON" : "OFF")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));
    }

    private Component getSoundText() {
        boolean enabled = BypassConfig.isSoundEffects();
        return Component.translatable("gui.mrkunbypass.config.sound_effects")
                .append(Component.literal(": ")
                        .append(Component.literal(enabled ? "ON" : "OFF")
                                .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xCC000000);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font,
                Component.literal("Mrkun Bypass").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font,
                Component.translatable("gui.mrkunbypass.config.settings").withStyle(ChatFormatting.WHITE),
                this.width / 2, 36, 0xAAAAAA);
    }

    @Override
    public void close() {
        this.minecraft.setScreen(parent);
    }
}
