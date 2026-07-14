package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WelcomeScreen extends Screen {
    private final Screen parent;

    public WelcomeScreen(Screen parent) {
        super(Component.translatable("gui.mrkunbypass.welcome.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 150;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height - 50;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.mrkunbypass.welcome.got_it"), btn -> this.close())
                .bounds(x, y, buttonWidth, 20)
                .build());
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xEE000000);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int y = 40;

        guiGraphics.drawCenteredString(this.font,
                Component.literal("Mrkun Bypass").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                centerX, y, 0xFFFFFF);
        y += 30;

        String[] lines = {
                "gui.mrkunbypass.welcome.line1",
                "gui.mrkunbypass.welcome.line2",
                "gui.mrkunbypass.welcome.line3",
                "",
                "gui.mrkunbypass.welcome.line4",
                "gui.mrkunbypass.welcome.line5",
                "",
                "gui.mrkunbypass.welcome.line6"
        };

        for (String line : lines) {
            if (line.isEmpty()) {
                y += 10;
            } else {
                guiGraphics.drawCenteredString(this.font,
                        Component.translatable(line).withStyle(ChatFormatting.WHITE),
                        centerX, y, 0xFFFFFF);
                y += 12;
            }
        }
    }

    @Override
    public void close() {
        BypassConfig.setFirstLaunch(false);
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }
}
