package kaptainwutax.tungsten.render;

import kaptainwutax.tungsten.path.Node;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

import java.util.List;

public final class PlayerHologram {

    private static final int TICKS_PER_FRAME = 1;

    private static long   animStartMs  = -1;
    private static int    frameIndex   = 0;
    public static void resetAnimation() {
        animStartMs = System.currentTimeMillis();
        frameIndex  = 0;
    }

    public static void render(List<Node> path, int currentTick) {
        if (path == null || path.isEmpty()) return;

        long now = System.currentTimeMillis();
        if (animStartMs < 0) animStartMs = now;

        int remaining = path.size() - currentTick;
        if (remaining <= 0) return;

        long elapsed = now - animStartMs;
        int  raw     = (int)(elapsed / 50);                 
        int  idx     = currentTick + (raw % remaining);    
        idx = Math.min(idx, path.size() - 1);

        Node node = path.get(idx);
        float progress = (float)(idx - currentTick) / Math.max(remaining - 1, 1); // 0.0 → 1.0


        int alpha  = 210;
        int r      = (int)(progress * 255);
        int g      = 230;
        int b      = (int)((1f - progress * 0.5f) * 255);
        Color col  = new Color(r, g, b);

        double speed  = Math.sqrt(node.agent.velX * node.agent.velX + node.agent.velZ * node.agent.velZ);
        float  swing  = (float)(Math.sin(idx * 0.5) * speed * 6.0);  

        drawSteve(node.agent.getPos(), node.agent.yaw, swing, col, alpha);
    }

    public static void drawSteve(Vec3d feet, float yaw, float swing, Color col, int alpha) {
        double x = feet.x, y = feet.y, z = feet.z;
        double yawRad = Math.toRadians(yaw);

        double rx =  Math.cos(yawRad);
        double rz =  Math.sin(yawRad);

        drawBox(new Vec3d(x, y + 1.575, z), 0.25, 0.225, 0.225, yaw, col, alpha);

        drawBox(new Vec3d(x, y + 1.00, z), 0.25, 0.30, 0.125, yaw, col, alpha);

        double lax = x - rx * 0.35;
        double laz = z - rz * 0.35;
        double armSwingOffset =  swing * 0.05;
        drawBox(new Vec3d(lax, y + 1.00 + armSwingOffset, laz), 0.11, 0.30, 0.11, yaw, col, alpha);

        double rax = x + rx * 0.35;
        double raz = z + rz * 0.35;
        drawBox(new Vec3d(rax, y + 1.00 - armSwingOffset, raz), 0.11, 0.30, 0.11, yaw, col, alpha);

        double legSwingOffset = -swing * 0.06;
        double llx = x - rx * 0.125;
        double llz = z - rz * 0.125;
        drawBox(new Vec3d(llx, y + 0.35 + legSwingOffset, llz), 0.125, 0.35, 0.125, yaw, col, alpha);

        double rlx = x + rx * 0.125;
        double rlz = z + rz * 0.125;
        drawBox(new Vec3d(rlx, y + 0.35 - legSwingOffset, rlz), 0.125, 0.35, 0.125, yaw, col, alpha);
    }

    private static void drawBox(Vec3d center, double hx, double hy, double hz,
                                 float yawDeg, Color col, int alpha) {
        double yawRad = Math.toRadians(yawDeg);
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);

        double[][] local = {
            {-hx, -hy, -hz}, { hx, -hy, -hz},
            { hx, -hy,  hz}, {-hx, -hy,  hz},
            {-hx,  hy, -hz}, { hx,  hy, -hz},
            { hx,  hy,  hz}, {-hx,  hy,  hz},
        };

        Vec3d[] w = new Vec3d[8];
        for (int i = 0; i < 8; i++) {
            double lx = local[i][0], ly = local[i][1], lz = local[i][2];
            double wx = lx * cos - lz * sin;
            double wz = lx * sin + lz * cos;
            w[i] = new Vec3d(center.x + wx, center.y + ly, center.z + wz);
        }

        int argb = col.toARGB(alpha);

        line(w[0], w[1], argb); line(w[1], w[2], argb);
        line(w[2], w[3], argb); line(w[3], w[0], argb); 
        line(w[4], w[5], argb); line(w[5], w[6], argb);
        line(w[6], w[7], argb); line(w[7], w[4], argb); 
        line(w[0], w[4], argb); line(w[1], w[5], argb);
        line(w[2], w[6], argb); line(w[3], w[7], argb); 
    }

    private static void line(Vec3d a, Vec3d b, int argb) {
        GizmoDrawing.line(a, b, argb).ignoreOcclusion();
    }
}
