package com.mrkun.bypass;

import com.mrkun.bypass.client.ClientInit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MrkunBypassNeoForge.MOD_ID)
public class MrkunBypassNeoForge {
    public static final String MOD_ID = "mrkunbypass";

    public MrkunBypassNeoForge(IEventBus modEventBus) {
        // 本模组在 neoforge.mods.toml 中声明 side=CLIENT，故 @Mod 构造只会在客户端执行。
        // 直接将模组事件总线交给客户端初始化逻辑（注册按键映射 / 运行时事件）。
        ClientInit.init(modEventBus);
    }
}
