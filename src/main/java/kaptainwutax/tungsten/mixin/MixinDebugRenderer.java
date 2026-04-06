package kaptainwutax.tungsten.mixin;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;

import kaptainwutax.tungsten.TungstenMod;
import kaptainwutax.tungsten.TungstenModDataContainer;
import kaptainwutax.tungsten.TungstenModRenderContainer;
import kaptainwutax.tungsten.path.Node;
import kaptainwutax.tungsten.render.Color;
import kaptainwutax.tungsten.render.Cuboid;
import kaptainwutax.tungsten.render.PlayerHologram;
import kaptainwutax.tungsten.render.Renderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;

@Mixin(DebugRenderer.class)
public class MixinDebugRenderer {
	
	private static final int MAX_RENDERERS_PER_CATEGORY = 500;

	private static List<Node> lastSeenPath = null;

	@Inject(method = "render", at = @At("RETURN"))
	public void render(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress, CallbackInfo ci) {
		
		glDisable(GL_DEPTH_TEST);
	    glDisable(GL_BLEND);
	    

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder;

		DrawStyle drawStyle = new DrawStyle(-1, 2.0F, 0);
		
		builder = tessellator.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
		Cuboid goal = new Cuboid(TungstenMod.TARGET.subtract(0.5D, 0D, 0.5D), new Vec3d(1.0D, 2.0D, 1.0D), Color.GREEN);
		goal.render(builder);

		if (!TungstenModRenderContainer.RUNNING_PATH_RENDERER.isEmpty())
			renderCollection(TungstenModRenderContainer.RUNNING_PATH_RENDERER, tessellator, frustum, cameraX, cameraY, cameraZ);

		if (!TungstenModRenderContainer.BLOCK_PATH_RENDERER.isEmpty())
			renderCollection(TungstenModRenderContainer.BLOCK_PATH_RENDERER, tessellator, frustum, cameraX, cameraY, cameraZ);

		if (!TungstenModRenderContainer.RENDERERS.isEmpty())
			renderCollection(TungstenModRenderContainer.RENDERERS, tessellator, frustum, cameraX, cameraY, cameraZ);

		if (!TungstenModRenderContainer.TEST.isEmpty())
			renderCollection(TungstenModRenderContainer.TEST, tessellator, frustum, cameraX, cameraY, cameraZ);
		
		if (!TungstenModRenderContainer.ERROR.isEmpty())
			renderCollection(TungstenModRenderContainer.ERROR, tessellator, frustum, cameraX, cameraY, cameraZ);

		try {
			List<Node> path = TungstenModDataContainer.EXECUTOR != null
					? TungstenModDataContainer.EXECUTOR.getPath()
					: null;

			if (path != null && !path.isEmpty()) {
				if (path != lastSeenPath) {
					PlayerHologram.resetAnimation();
					lastSeenPath = path;
				}
				int currentTick = TungstenModDataContainer.EXECUTOR.getCurrentTick();
				PlayerHologram.render(path, currentTick);
			} else {
				lastSeenPath = null;
			}
		} catch (Exception e) {
			TungstenMod.LOG.debug("[Tungsten] Hologram render error: " + e.getMessage());
		}

	    glEnable(GL_BLEND);
	    glEnable(GL_DEPTH_TEST);
	}

	private static void renderCollection(Collection<Renderer> renderers, Tessellator tessellator, Frustum frustum,
			double cameraX, double cameraY, double cameraZ) {
		int count = 0;
		List<Renderer> sortedRenderers = new ArrayList<>(renderers);
		Collections.reverse(sortedRenderers);
		try {
			for (Renderer r : sortedRenderers) {
				if (count >= MAX_RENDERERS_PER_CATEGORY) {
					break;
				}
	
				try {
					if (r.getPos() != null) {
						if (!frustum.isVisible(new Box(r.getPos().getX() - 3, r.getPos().getY() - 3, r.getPos().getZ() - 3,
								r.getPos().getX() + 3, r.getPos().getY() + 3, r.getPos().getZ() + 3))) {
							continue;
						}
					}
	
					BufferBuilder b = tessellator.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
					r.render(b);
					count++;
				} catch (Exception e) {
					TungstenMod.LOG.debug("Error rendering object: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			TungstenMod.LOG.debug("Error rendering object: " + e.getMessage());
		}
	}

	private static void render(Renderer r, Tessellator tessellator) {
		try {
			BufferBuilder builder = tessellator.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
			r.render(builder);
		} catch (Exception e) {
		}
	}

}
