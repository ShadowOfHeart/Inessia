package gg.vape.module.impl.Render;

import gg.vape.helpers.render.ColorUtil;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.settings.options.ColorSetting;
import gg.vape.settings.options.ModeSetting;
import gg.vape.settings.options.SliderSetting;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventRender;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_GREATER;
@ModuleInfo(name = "Trails", type = Category.Render)
public class Trails extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Fade", "Astolfo", "Rainbow").call(this);
    public ColorSetting color = new ColorSetting("Color", -1).setHidden(() -> !(mode.is("Static") || mode.is("Fade"))).call(this);
    public SliderSetting time = new SliderSetting("Time", 500, 0, 1500, 100);
    public ArrayList<Point> p = new ArrayList<>();

    public Trails() {
        addSettings(time);
    }

   

    @EventTarget
    public void onRender(EventRender e) {


        double ix = - (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.pt);
        double iy = - (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.pt);
        double iz = - (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.pt);
        // interpolated player position
        float x = (float) ((float) ((float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.pt)));
        float y = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.pt);
        float z = (float) ((float) ((float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.pt)));
        //update
        p.add(new Point(new Vec3d(x, y, z)));
        p.removeIf(point -> point.time >= time.get());

        GlStateManager.pushMatrix();

        GL11.glDepthMask(false);

        GlStateManager.translate(ix, iy, iz);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GlStateManager.alphaFunc(GL_GREATER, 0.0F);
        // trail
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        for (Point point : p) {
            if (p.indexOf(point) >= p.size() - 1) continue;
            float alpha = 100 * (p.indexOf(point) / (float) p.size());
            Point temp = p.get(p.indexOf(point) + 1);
            int color = RenderUtil.setAlpha(ColorUtil.applyColor(mode.get(), p.indexOf(point) * 4, this.color.get(), 255), (int) alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
            GL11.glVertex3d(temp.pos.x, temp.pos.y + mc.player.height - 0.1, temp.pos.z);
            point.time++;
        }
        GL11.glEnd();

        // line
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (Point point : p) {
            if (p.indexOf(point) >= p.size() - 1) continue;
            float alpha = 100 * (p.indexOf(point) / (float) p.size());
            Point temp = p.get(p.indexOf(point) + 1);
            int color = RenderUtil.setAlpha(ColorUtil.applyColor(mode.get(), p.indexOf(point) * 4, this.color.get(), 255).brighter(), (int) alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);

            point.time++;
        }
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (Point point : p) {
            if (p.indexOf(point) >= p.size() - 1) continue;
            float alpha = 100 * (p.indexOf(point) / (float) p.size());
            Point temp = p.get(p.indexOf(point) + 1);
            int color = RenderUtil.setAlpha(ColorUtil.applyColor(mode.get(), p.indexOf(point) * 4, this.color.get(), 255).brighter(), (int) alpha);
            RenderUtil.color(color);
            GL11.glVertex3d(temp.pos.x, temp.pos.y + mc.player.height - 0.1, temp.pos.z);
            point.time++;
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        GlStateManager.popMatrix();


    }

    public class Point {
        public Vec3d pos;
        public long time;

        public Point(Vec3d pos) {
            this.pos = pos;
        }
    }

}
