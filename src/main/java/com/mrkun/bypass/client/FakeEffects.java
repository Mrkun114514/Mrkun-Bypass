package com.mrkun.bypass.client;

import com.mrkun.bypass.config.BypassConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Set;

/**
 * 假开挂视觉特效（纯客户端，不改玩法）：
 * 1) 锁定类模块（KillAura / AimBot / Reach）开启时，在最近实体处绘制旋转红色锁定环 + 周期性命中粒子。
 * 2) 任意模块切换时，播放「正在绕过反作弊… X%」进度条动画，完成后弹「✓ 已绕过 <反作弊名>」提示。
 *
 * 2D 投影方案：在世界渲染事件中捕获 view/projection 矩阵，在 HUD 回调里把实体世界坐标投到屏幕，
 * 再用 DrawContext.fill 画点成环。完全避开跨版本（1.20.1~1.21.11）不稳定的 Tesselator 线渲染 API。
 */
public class FakeEffects {
    // 触发锁定环/命中粒子的"锁定类"模块
    private static final Set<String> TARGETING_MODULES = Set.of("KillAura", "AimBot", "Reach");

    // 每帧捕获的 3D 矩阵，用于把实体世界坐标投影到屏幕
    private static Matrix4f viewMatrix;
    private static Matrix4f projMatrix;

    // 假反作弊绕过动画状态
    private static final long BYPASS_DURATION = 1600;
    private static final long TOAST_DURATION = 2400;
    private static long bypassStart = -1;
    private static long toastStart = -1;
    private static String bypassName = "";
    private static String toastText = "";
    private static final String[] ANTI_CHEATS = {"Matrix", "Verus", "Vulcan", "AAC", "Spartan", "Grim", "Negativity", "Kauri", "BAC"};

    private static long lastParticleTime = 0;

    public static void captureFrame(Matrix4f view, Matrix4f proj) {
        viewMatrix = view;
        projMatrix = proj;
    }

    /** 任意模块切换时触发假反作弊绕过动画 */
    public static void onModuleToggled(String moduleName, boolean enabled) {
        bypassStart = System.currentTimeMillis();
        bypassName = ANTI_CHEATS[(int) (Math.random() * ANTI_CHEATS.length)];
        toastStart = -1;
    }

    public static void renderHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        int w = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();

        // 锁定环 + 命中粒子（仅游戏中、无界面时）
        if (client.currentScreen == null && targetingActive()) {
            Entity target = findNearestTarget(client);
            if (target != null) {
                drawTargetingRing(context, w, h, target);
                spawnHitParticles(client, target);
            }
        }

        // 假反作弊绕过进度条 + 完成提示
        renderBypass(context, w, h);
    }

    private static boolean targetingActive() {
        for (String m : TARGETING_MODULES) {
            if (BypassConfig.isModuleEnabled(m)) return true;
        }
        return false;
    }

    private static Entity findNearestTarget(MinecraftClient client) {
        if (client.world == null) return null;
        Entity player = client.player;
        Entity best = null;
        double bestDist = 6.0;
        for (Entity e : client.world.getEntities()) {
            if (e == player || e == player.getVehicle()) continue;
            if (!(e instanceof LivingEntity)) continue;
            if (!e.isAlive()) continue;
            double d = player.distanceTo(e);
            if (d < bestDist) {
                bestDist = d;
                best = e;
            }
        }
        return best;
    }

    private static void drawTargetingRing(DrawContext context, int w, int h, Entity target) {
        if (viewMatrix == null || projMatrix == null) return;
        double cy = target.getY() + target.getHeight() * 0.5;
        Vector4f v = new Vector4f((float) target.getX(), (float) cy, (float) target.getZ(), 1.0f);
        v.mul(viewMatrix);
        v.mul(projMatrix);
        if (v.w <= 0.0001) return;
        float sx = (v.x / v.w * 0.5f + 0.5f) * w;
        float sy = (1 - (v.y / v.w * 0.5f + 0.5f)) * h;
        if (sx < -40 || sx > w + 40 || sy < -40 || sy > h + 40) return;

        float radius = 16 + (float) (Math.sin(System.currentTimeMillis() / 200.0) * 1.5f);
        float rotation = (System.currentTimeMillis() / 600f) % (float) (Math.PI * 2);
        int color = 0xFFFF4444;
        int points = 36;
        for (int i = 0; i <= points; i++) {
            float a = rotation + (float) i / points * (float) Math.PI * 2f;
            int x = (int) (sx + Math.cos(a) * radius);
            int y = (int) (sy + Math.sin(a) * radius);
            context.fill(x, y, x + 2, y + 2, color);
        }
    }

    private static void spawnHitParticles(MinecraftClient client, Entity target) {
        long now = System.currentTimeMillis();
        if (now - lastParticleTime < 220) return;
        lastParticleTime = now;
        client.world.addParticle(ParticleTypes.DAMAGE_INDICATOR,
                target.getX(), target.getY() + target.getHeight() * 0.5, target.getZ(), 0.0, 0.15, 0.0);
    }

    private static void renderBypass(DrawContext context, int w, int h) {
        long now = System.currentTimeMillis();
        if (bypassStart > 0) {
            long el = now - bypassStart;
            if (el < BYPASS_DURATION) {
                int pct = (int) (el * 100 / BYPASS_DURATION);
                drawBypassBar(context, w, h, pct, bypassName);
            } else {
                bypassStart = -1;
                toastStart = now;
                toastText = "✓ 已绕过 " + bypassName;
            }
        }
        if (toastStart > 0) {
            long el = now - toastStart;
            if (el < TOAST_DURATION) {
                drawToast(context, w, h, toastText);
            } else {
                toastStart = -1;
            }
        }
    }

    private static void drawBypassBar(DrawContext context, int w, int h, int pct, String acName) {
        int barW = 230, barH = 14;
        int bx = (w - barW) / 2;
        int by = (int) (h * 0.30);
        context.fill(bx - 3, by - 3, bx + barW + 3, by + barH + 3, 0xCC000000);
        String label = "正在绕过反作弊 " + acName + "… " + pct + "%";
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(label), w / 2, by - 14, 0xFF66FF99);
        context.fill(bx, by, bx + barW, by + barH, 0x55000000);
        int fillW = (int) (barW * pct / 100.0);
        context.fill(bx, by, bx + fillW, by + barH, 0xFF33FF77);
    }

    private static void drawToast(DrawContext context, int w, int h, String text) {
        int tw = MinecraftClient.getInstance().textRenderer.getWidth(text) + 28;
        int th = 24;
        int tx = (w - tw) / 2;
        int ty = (int) (h * 0.40);
        context.fill(tx, ty, tx + tw, ty + th, 0xDD0E1A12);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(text), w / 2, ty + 7, 0xFF55FF99);
    }
}
