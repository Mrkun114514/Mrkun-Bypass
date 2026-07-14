package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("gui.mrkunbypass.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 - 60;

        ButtonWidget startupButton = ButtonWidget.builder(
                        getStartupText(),
                        btn -> {
                            BypassConfig.setShowStartupNotification(!BypassConfig.isShowStartupNotification());
                            btn.setMessage(getStartupText());
                        })
                .dimensions(x, y, buttonWidth, 20)
                .build();
        this.addDrawableChild(startupButton);

        y += 25;

        ButtonWidget blurButton = ButtonWidget.builder(
                        getBlurText(),
                        btn -> {
                            BypassConfig.setBackgroundBlur(!BypassConfig.isBackgroundBlur());
                            btn.setMessage(getBlurText());
                        })
                .dimensions(x, y, buttonWidth, 20)
                .build();
        this.addDrawableChild(blurButton);

        y += 25;

        ButtonWidget statisticsButton = ButtonWidget.builder(
                        getStatisticsText(),
                        btn -> {
                            BypassConfig.setShowStatistics(!BypassConfig.isShowStatistics());
                            btn.setMessage(getStatisticsText());
                        })
                .dimensions(x, y, buttonWidth, 20)
                .build();
        this.addDrawableChild(statisticsButton);

        y += 25;

        ButtonWidget soundButton = ButtonWidget.builder(
                        getSoundText(),
                        btn -> {
                            BypassConfig.setSoundEffects(!BypassConfig.isSoundEffects());
                            btn.setMessage(getSoundText());
                        })
                .dimensions(x, y, buttonWidth, 20)
                .build();
        this.addDrawableChild(soundButton);

        y += 40;

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.mrkunbypass.config.done"), btn -> this.close())
                .dimensions(x, y, buttonWidth, 20)
                .build());
    }

    private Text getStartupText() {
        boolean enabled = BypassConfig.isShowStartupNotification();
        return Text.translatable("gui.mrkunbypass.config.startup_notification")
                .append(Text.literal(": ")
                        .append(Text.literal(enabled ? "ON" : "OFF")
                                .formatted(enabled ? Formatting.GREEN : Formatting.RED)));
    }

    private Text getBlurText() {
        boolean enabled = BypassConfig.isBackgroundBlur();
        return Text.translatable("gui.mrkunbypass.config.background_blur")
                .append(Text.literal(": ")
                        .append(Text.literal(enabled ? "ON" : "OFF")
                                .formatted(enabled ? Formatting.GREEN : Formatting.RED)));
    }

    private Text getStatisticsText() {
        boolean enabled = BypassConfig.isShowStatistics();
        return Text.translatable("gui.mrkunbypass.config.statistics")
                .append(Text.literal(": ")
                        .append(Text.literal(enabled ? "ON" : "OFF")
                                .formatted(enabled ? Formatting.GREEN : Formatting.RED)));
    }

    private Text getSoundText() {
        boolean enabled = BypassConfig.isSoundEffects();
        return Text.translatable("gui.mrkunbypass.config.sound_effects")
                .append(Text.literal(": ")
                        .append(Text.literal(enabled ? "ON" : "OFF")
                                .formatted(enabled ? Formatting.GREEN : Formatting.RED)));
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Mrkun Bypass").formatted(Formatting.GOLD, Formatting.BOLD),
                this.width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.config.settings").formatted(Formatting.WHITE),
                this.width / 2, 36, 0xAAAAAA);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}