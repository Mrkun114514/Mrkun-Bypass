package com.mrkun.bypass.client;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;

public class BypassMessage {

    public static void sendModuleToggleMessage(String moduleName, boolean enabled) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        MutableComponent prefix = Component.literal("Mrkun Bypass").withStyle(ChatFormatting.WHITE);
        MutableComponent separator = Component.literal("：").withStyle(ChatFormatting.WHITE);
        MutableComponent module = Component.literal(moduleName).withStyle(ChatFormatting.RED);
        MutableComponent status = Component.literal(enabled ? "已开启！" : "已关闭！").withStyle(ChatFormatting.RED);

        MutableComponent message = prefix.append(separator).append(module)
                .append(Component.literal(" ").withStyle(ChatFormatting.RESET)).append(status);
        mc.player.sendSystemMessage(message);

        if (BypassConfig.isSoundEffects()) {
            if (enabled) {
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BLOCK_NOTE_BLOCK_CHIME, 1.5f));
            } else {
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BLOCK_NOTE_BLOCK_BASS, 0.5f));
            }
        }
    }
}
