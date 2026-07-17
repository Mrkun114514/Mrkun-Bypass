package com.mrkun.bypass.client;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BypassMessage {

    public static void sendModuleToggleMessage(String moduleName, boolean enabled) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        MutableText prefix = Text.translatable("message.mrkunbypass.prefix").formatted(Formatting.WHITE);
        MutableText separator = Text.translatable("message.mrkunbypass.separator").formatted(Formatting.WHITE);
        MutableText module = Text.translatable("key.mrkunbypass." + moduleName.toLowerCase()).formatted(Formatting.RED);
        MutableText status = Text.translatable(enabled ? "message.mrkunbypass.on" : "message.mrkunbypass.off").formatted(Formatting.RED);

        MutableText message = prefix.append(separator).append(module).append(Text.literal(" ").formatted(Formatting.RESET)).append(status);
        client.player.sendMessage(message, false);

        if (BypassConfig.isSoundEffects()) {
            if (enabled) {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), 1.5f));
            } else {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(), 0.5f));
            }
        }

        FakeEffects.onModuleToggled(moduleName, enabled);
    }
}