package com.mrkun.bypass.gui;

import com.mrkun.bypass.client.BypassMessage;
import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BypassMenuScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 25;
    private static final int BUTTON_SPACING = 5;
    private static final int TOP_MARGIN = 66;
    private static final int BOTTOM_MARGIN = 40;
    private static final int STATISTICS_HEIGHT = 60;

    private final java.util.List<ModuleButton> moduleButtons = new java.util.ArrayList<>();
    private int scrollOffset = 0;
    private int contentHeight = 0;
    private int listTop;
    private int listBottom;
    private Button configButton;

    public BypassMenuScreen() {
        super(Component.translatable("gui.mrkunbypass.menu.title"));
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
                        BypassMessage.sendModuleToggleMessage(moduleName, BypassConfig.isModuleEnabled(moduleName));
                        updateButtonStates();
                    }
            );
            moduleButtons.add(button);
            this.addRenderableWidget(button);
        }

        configButton = Button.builder(Component.translatable("gui.mrkunbypass.menu.settings"), btn -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new ConfigScreen(this));
                    }
                })
                .bounds(this.width - 90, this.height - 30, 80, 20)
                .build();
        this.addRenderableWidget(configButton);

        contentHeight = modules.length * (BUTTON_HEIGHT + BUTTON_SPACING) - BUTTON_SPACING;
        clampScrollOffset();
        layoutButtons();
        updateButtonStates();
    }

    private void clampScrollOffset() {
        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        scrollOffset = net.minecraft.util.Mth.clamp(scrollOffset, 0, maxScroll);
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
    public void resize(net.minecraft.client.Minecraft client, int width, int height) {
        super.resize(client, width, height);
        listTop = TOP_MARGIN;
        listBottom = this.height - BOTTOM_MARGIN - (BypassConfig.isShowStatistics() ? STATISTICS_HEIGHT : 0);
        clampScrollOffset();
        layoutButtons();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        if (maxScroll > 0) {
            scrollOffset = net.minecraft.util.Mth.clamp(scrollOffset - (int) (scrollY * 20), 0, maxScroll);
            layoutButtons();
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.minecraft != null && this.minecraft.level != null) {
            guiGraphics.fill(0, 0, this.width, this.height, 0xCC000000);
        } else {
            super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // 顶部主题色带 + 随时间脉动的霓虹强调线（让界面更有辨识度）
        guiGraphics.fillGradient(0, 0, this.width, 50, 0xFF1A1330, 0xFF0B0B14);
        int pulse = 140 + (int) (Math.sin(System.currentTimeMillis() / 350.0) * 100);
        int accent = (0xFF << 24) | (0x9B << 16) | (0x5C << 8) | Math.min(255, pulse);
        guiGraphics.fill(0, 49, this.width, 51, accent);

        guiGraphics.drawCenteredString(BypassMenuScreen.this.font,
                Component.literal("Mrkun").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD)
                        .append(Component.literal(" Bypass").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)),
                this.width / 2, 12, 0xFFFFFF);

        guiGraphics.drawCenteredString(BypassMenuScreen.this.font,
                Component.literal("// ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.translatable("gui.mrkunbypass.menu.title").withStyle(ChatFormatting.GRAY)),
                this.width / 2, 30, 0xAAAAAA);

        guiGraphics.drawCenteredString(BypassMenuScreen.this.font,
                Component.translatable("gui.mrkunbypass.menu.close_hint").withStyle(ChatFormatting.DARK_GRAY),
                this.width / 2, 58, 0x888888);

        int listHeight = Math.max(0, listBottom - listTop);
        int maxScroll = Math.max(0, contentHeight - listHeight);
        if (maxScroll > 0) {
            int trackHeight = listHeight;
            int thumbHeight = Math.max(20, listHeight * listHeight / contentHeight);
            int thumbY = listTop + (scrollOffset * (trackHeight - thumbHeight) / maxScroll);
            int scrollbarX = (this.width + BUTTON_WIDTH) / 2 + 8;
            guiGraphics.fill(scrollbarX, listTop, scrollbarX + 4, listBottom, 0x40FFFFFF);
            guiGraphics.fill(scrollbarX, thumbY, scrollbarX + 4, thumbY + thumbHeight, 0xFFFFFFFF);
        }

        guiGraphics.enableScissor(0, listTop, this.width, listBottom);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.disableScissor();

        if (BypassConfig.isShowStatistics()) {
            renderStatistics(guiGraphics);
        }
    }

    private void renderStatistics(GuiGraphics guiGraphics) {
        int statsY = this.height - BOTTOM_MARGIN - STATISTICS_HEIGHT + 10;
        int centerX = this.width / 2;

        guiGraphics.drawCenteredString(this.font,
                Component.translatable("gui.mrkunbypass.statistics.title").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                centerX, statsY, 0xFFFFFF);
        statsY += 14;

        guiGraphics.drawString(this.font,
                Component.translatable("gui.mrkunbypass.statistics.modules_used").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(String.valueOf(BypassConfig.getActiveModuleCount()) + "/" + BypassConfig.getModuleNames().length).withStyle(ChatFormatting.GREEN)),
                centerX - 80, statsY, 0xFFFFFF);

        guiGraphics.drawString(this.font,
                Component.translatable("gui.mrkunbypass.statistics.toggles_total").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(String.valueOf(BypassConfig.getTotalToggles())).withStyle(ChatFormatting.YELLOW)),
                centerX + 20, statsY, 0xFFFFFF);
        statsY += 14;

        guiGraphics.drawString(this.font,
                Component.translatable("gui.mrkunbypass.statistics.time_played").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(BypassConfig.getSessionTime()).withStyle(ChatFormatting.BLUE)),
                centerX - 30, statsY, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 72) {
            if (this.minecraft != null) this.minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private class ModuleButton extends Button {
        private final String moduleName;

        public ModuleButton(int x, int y, int width, int height, String moduleName, OnPress onPress) {
            super(x, y, width, height,
                    Component.translatable("key.mrkunbypass." + moduleName.toLowerCase()),
                    onPress,
                    (button) -> Component.translatable("key.mrkunbypass." + moduleName.toLowerCase()));
            this.moduleName = moduleName;
            updateState();
        }

        public void updateState() {
            boolean enabled = BypassConfig.isModuleEnabled(moduleName);
            ChatFormatting statusColor = enabled ? ChatFormatting.GREEN : ChatFormatting.RED;
            ChatFormatting bracketColor = ChatFormatting.GRAY;
            String status = enabled ? "ON" : "OFF";
            String arrow = enabled ? ">> " : " ";
            this.setMessage(Component.literal(arrow).withStyle(ChatFormatting.WHITE)
                    .append(Component.translatable("key.mrkunbypass." + moduleName.toLowerCase()).withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" [").withStyle(bracketColor))
                    .append(Component.literal(status).withStyle(statusColor, ChatFormatting.BOLD))
                    .append(Component.literal("]").withStyle(bracketColor)));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!this.visible) return;
            updateState(); // 每帧同步开关状态，避免用热键切换后菜单文字不刷新

            boolean enabled = BypassConfig.isModuleEnabled(moduleName);
            int catColor = BypassConfig.getCategoryColor(moduleName);
            int bgColor;
            int bgHoverColor;

            if (this.isHovered()) {
                bgColor = enabled ? 0x80104050 : 0x80202028;
                bgHoverColor = enabled ? 0xA0185058 : 0xA028282F;
            } else {
                bgColor = enabled ? 0x500A2A30 : 0x50141418;
                bgHoverColor = bgColor;
            }

            // 启用时的外发光（品类色脉动）
            if (enabled) {
                int glow = 50 + (int) (Math.sin(System.currentTimeMillis() / 220.0) * 35);
                int glowCol = (glow << 24) | (catColor & 0x00FFFFFF);
                guiGraphics.fill(this.getX() - 2, this.getY() - 2,
                        this.getX() + this.width + 2, this.getY() + this.height + 2, glowCol);
            }

            guiGraphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor, bgHoverColor);

            // 左侧分类色条
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + 4, this.getY() + this.height, catColor);

            // 边框：启用时用品类色脉动，否则暗灰
            int border = enabled ? catColor : 0xFF3A3A42;
            if (enabled) {
                int a = 0x80 + (int) (Math.sin(System.currentTimeMillis() / 220.0) * 0x60);
                border = (a << 24) | (catColor & 0x00FFFFFF);
            }
            guiGraphics.renderOutline(this.getX(), this.getY(), this.width, this.height, border);

            // 主文本（右移避开色条）
            guiGraphics.drawCenteredString(BypassMenuScreen.this.font, this.getMessage(),
                    this.getX() + this.width / 2 + 2,
                    this.getY() + (this.height - 8) / 2,
                    0xFFFFFF);

            // 右下角分类标签
            String cat = BypassConfig.getCategory(moduleName).toUpperCase();
            guiGraphics.drawString(BypassMenuScreen.this.font, cat,
                    this.getX() + this.width - BypassMenuScreen.this.font.width(cat) - 6,
                    this.getY() + this.height - 11,
                    catColor);
        }
    }
}
