package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WelcomeScreen extends Screen {
    private final Screen parent;

    public WelcomeScreen(Screen parent) {
        super(Text.translatable("gui.mrkunbypass.welcome.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 150;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height - 50;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.mrkunbypass.welcome.got_it"), btn -> this.close())
                .dimensions(x, y, buttonWidth, 20)
                .build());
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xEE000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int y = 40;

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Mrkun Bypass").formatted(Formatting.GOLD, Formatting.BOLD),
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
                context.drawCenteredTextWithShadow(this.textRenderer,
                        Text.translatable(line).formatted(Formatting.WHITE),
                        centerX, y, 0xFFFFFF);
                y += 12;
            }
        }
    }

    @Override
    public void close() {
        BypassConfig.setFirstLaunch(false);
        if (this.client != null) {
            this.client.setScreen(null);
        }
    }
}