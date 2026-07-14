package com.mrkun.bypass.gui;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BypassMenuScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 25;
    private static final int BUTTON_SPACING = 5;
    private static final int TOP_MARGIN = 60;
    private static final int BOTTOM_MARGIN = 40;
    private static final int STATISTICS_HEIGHT = 60;

    private final java.util.List<ModuleButton> moduleButtons = new java.util.ArrayList<>();
    private int scrollOffset = 0;
    private int contentHeight = 0;
    private int listTop;
    private int listBottom;
    private ButtonWidget configButton;

    public BypassMenuScreen() {
        super(Text.translatable("gui.mrkunbypass.menu.title"));
    }

    @Override
    protected void init() {
        moduleButtons.clear();
        String[] modules = BypassConfig.getModuleNames();

        listTop = TOP_MARGIN;
        listBottom = this.height - BOTTOM_MARGIN - (BypassConfig.isShowStatistics() ? STATISTICS_HEIGHT : 0);
        int x = (this.width - BUTTON_WIDTH) / 2;

        for (int i = 0; i < modules.length; i++) {
            String moduleName = modules[i];
            ModuleButton button = new ModuleButton(
                    x, 0, BUTTON_WIDTH, BUTTON_HEIGHT,
                    moduleName,
                    btn -> {
                        BypassConfig.toggleModule(moduleName);
                        com.mrkun.bypass.client.BypassMessage.sendModuleToggleMessage(moduleName, BypassConfig.isModuleEnabled(moduleName));
                        updateButtonStates();
                    }
            );
            moduleButtons.add(button);
            this.addDrawableChild(button);
        }

        configButton = ButtonWidget.builder(Text.translatable("gui.mrkunbypass.menu.settings"), btn -> {
                    if (this.client != null) {
                        this.client.setScreen(new ConfigScreen(this));
                    }
                })
                .dimensions(this.width - 90, this.height - 30, 80, 20)
                .build();
        this.addDrawableChild(configButton);

        contentHeight = modules.length * (BUTTON_HEIGHT + BUTTON_SPACING) - BUTTON_SPACING;
        clampScrollOffset();
        layoutButtons();
        updateButtonStates();
    }

    private void clampScrollOffset() {
        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        scrollOffset = net.minecraft.util.math.MathHelper.clamp(scrollOffset, 0, maxScroll);
    }

    private void layoutButtons() {
        int x = (this.width - BUTTON_WIDTH) / 2;
        for (int i = 0; i < moduleButtons.size(); i++) {
            ModuleButton button = moduleButtons.get(i);
            int y = listTop + i * (BUTTON_HEIGHT + BUTTON_SPACING) - scrollOffset;
            button.setX(x);
            button.setY(y);
            button.visible = y + BUTTON_HEIGHT > listTop && y < listBottom;
        }
    }

    private void updateButtonStates() {
        for (ModuleButton button : moduleButtons) {
            button.updateState();
        }
    }

    @Override
    public void resize(net.minecraft.client.MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        listTop = TOP_MARGIN;
        listBottom = this.height - BOTTOM_MARGIN - (BypassConfig.isShowStatistics() ? STATISTICS_HEIGHT : 0);
        clampScrollOffset();
        layoutButtons();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        if (maxScroll > 0) {
            scrollOffset = net.minecraft.util.math.MathHelper.clamp(scrollOffset - (int) (verticalAmount * 20), 0, maxScroll);
            layoutButtons();
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client != null && this.client.world != null) {
            context.fill(0, 0, this.width, this.height, 0xCC000000);
        } else {
            super.renderBackground(context, mouseX, mouseY, delta);
        }

        if (BypassConfig.isBackgroundBlur() && this.client != null && this.client.world != null) {
            this.applyBlur(delta);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Mrkun Bypass").formatted(Formatting.WHITE, Formatting.BOLD)
                        .append(Text.literal(" | ").formatted(Formatting.GRAY))
                        .append(Text.translatable("gui.mrkunbypass.menu.title").formatted(Formatting.RED)),
                this.width / 2, 20, 0xFFFFFF);

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.menu.close_hint").formatted(Formatting.GRAY),
                this.width / 2, 36, 0xAAAAAA);

        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        if (maxScroll > 0) {
            int trackHeight = listHeight;
            int thumbHeight = Math.max(20, listHeight * listHeight / contentHeight);
            int thumbY = listTop + (scrollOffset * (trackHeight - thumbHeight) / maxScroll);
            int scrollbarX = (this.width + BUTTON_WIDTH) / 2 + 8;
            context.fill(scrollbarX, listTop, scrollbarX + 4, listBottom, 0x40FFFFFF);
            context.fill(scrollbarX, thumbY, scrollbarX + 4, thumbY + thumbHeight, 0xFFFFFFFF);
        }

        context.enableScissor(0, listTop, this.width, listBottom);
        super.render(context, mouseX, mouseY, delta);
        context.disableScissor();

        if (BypassConfig.isShowStatistics()) {
            renderStatistics(context);
        }
    }

    private void renderStatistics(DrawContext context) {
        int statsY = this.height - BOTTOM_MARGIN - STATISTICS_HEIGHT + 10;
        int centerX = this.width / 2;

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.statistics.title").formatted(Formatting.GOLD, Formatting.BOLD),
                centerX, statsY, 0xFFFFFF);
        statsY += 14;

        context.drawTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.statistics.modules_used").formatted(Formatting.GRAY)
                        .append(Text.literal(": ").formatted(Formatting.WHITE))
                        .append(Text.literal(String.valueOf(BypassConfig.getActiveModuleCount()) + "/" + BypassConfig.getModuleNames().length).formatted(Formatting.GREEN)),
                centerX - 80, statsY, 0xFFFFFF);

        context.drawTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.statistics.toggles_total").formatted(Formatting.GRAY)
                        .append(Text.literal(": ").formatted(Formatting.WHITE))
                        .append(Text.literal(String.valueOf(BypassConfig.getTotalToggles())).formatted(Formatting.YELLOW)),
                centerX + 20, statsY, 0xFFFFFF);
        statsY += 14;

        context.drawTextWithShadow(this.textRenderer,
                Text.translatable("gui.mrkunbypass.statistics.time_played").formatted(Formatting.GRAY)
                        .append(Text.literal(": ").formatted(Formatting.WHITE))
                        .append(Text.literal(BypassConfig.getSessionTime()).formatted(Formatting.BLUE)),
                centerX - 30, statsY, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 72) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private class ModuleButton extends ButtonWidget {
        private final String moduleName;

        public ModuleButton(int x, int y, int width, int height, String message, PressAction onPress) {
            super(x, y, width, height, Text.literal(message), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
            this.moduleName = message;
            updateState();
        }

        public void updateState() {
            boolean enabled = BypassConfig.isModuleEnabled(moduleName);
            Formatting statusColor = enabled ? Formatting.GREEN : Formatting.RED;
            Formatting bracketColor = Formatting.GRAY;
            String status = enabled ? "ON" : "OFF";
            String arrow = enabled ? ">> " : " ";
            this.setMessage(Text.literal(arrow + moduleName + " ").formatted(Formatting.WHITE)
                    .append(Text.literal("[").formatted(bracketColor))
                    .append(Text.literal(status).formatted(statusColor, Formatting.BOLD))
                    .append(Text.literal("]").formatted(bracketColor)));
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (!this.visible) return;

            boolean enabled = BypassConfig.isModuleEnabled(moduleName);
            int bgColor;
            int bgHoverColor;

            if (this.isHovered()) {
                bgColor = enabled ? 0x8000AA00 : 0x80AA0000;
                bgHoverColor = enabled ? 0xA000DD00 : 0xA0DD0000;
            } else {
                bgColor = enabled ? 0x50006600 : 0x50660000;
                bgHoverColor = bgColor;
            }

            context.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor, bgHoverColor);

            int borderColor = enabled ? 0xFF55FF55 : 0xFFFF5555;
            context.drawBorder(this.getX(), this.getY(), this.width, this.height, borderColor);

            context.drawCenteredTextWithShadow(textRenderer, this.getMessage(),
                    this.getX() + this.width / 2,
                    this.getY() + (this.height - 8) / 2,
                    0xFFFFFF);
        }
    }
}