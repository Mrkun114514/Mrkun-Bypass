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

        MutableComponent prefix = Component.translatable("message.mrkunbypass.prefix").withStyle(ChatFormatting.WHITE);
        MutableComponent separator = Component.translatable("message.mrkunbypass.separator").withStyle(ChatFormatting.WHITE);
        MutableComponent module = Component.translatable("key.mrkunbypass." + moduleName.toLowerCase()).withStyle(ChatFormatting.RED);
        MutableComponent status = Component.translatable(enabled ? "message.mrkunbypass.on" : "message.mrkunbypass.off").withStyle(ChatFormatting.RED);

        MutableComponent message = prefix.append(separator).append(module)
                .append(Component.literal(" ").withStyle(ChatFormatting.RESET)).append(status);
        mc.player.displayClientMessage(message, false);

        if (BypassConfig.isSoundEffects()) {
            if (enabled) {
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_CHIME, 1.5f));
            } else {
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BASS, 0.5f));
            }
        }

        FakeEffects.onModuleToggled(moduleName, enabled);
    }
}
