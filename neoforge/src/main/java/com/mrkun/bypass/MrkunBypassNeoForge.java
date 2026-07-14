package com.mrkun.bypass;

import com.mrkun.bypass.client.ClientInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;

@Mod(MrkunBypassNeoForge.MOD_ID)
public class MrkunBypassNeoForge {
    public static final String MOD_ID = "mrkunbypass";

    public MrkunBypassNeoForge() {
        // 仅客户端逻辑；服务端加载本模组时不会触碰任何客户端类
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);
    }
}
