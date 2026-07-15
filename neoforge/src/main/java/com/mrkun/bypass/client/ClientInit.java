package com.mrkun.bypass.client;

import com.mrkun.bypass.config.BypassConfig;
import com.mrkun.bypass.gui.ClientEvents;
import com.mrkun.bypass.keybind.KeyBindings;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class ClientInit {
    public static void init(IEventBus modEventBus) {
        BypassConfig.load();

        // 按键映射注册在模组事件总线（mod bus）上
        modEventBus.register(KeyBindings.class);

        // 游戏运行时事件（tick / 渲染 / 进服）注册在游戏事件总线（NeoForge bus）上
        NeoForge.EVENT_BUS.register(new ClientEvents());
    }
}
