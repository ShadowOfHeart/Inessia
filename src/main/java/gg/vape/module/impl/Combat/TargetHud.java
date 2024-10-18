package gg.vape.module.impl.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.helpers.StencilUtil;
import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.Direction;
import gg.vape.helpers.animation.impl.EaseBackIn;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.math.MathHelper;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.module.impl.Render.ClickGui;
import gg.vape.settings.options.BooleanSetting;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.event.events.impl.EventRender;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

@ModuleInfo(name = "TargetHud", type = Category.Hud)
public class TargetHud extends Module {

    private final HashMap<EntityPlayer, float[]> wtf = new HashMap();

    public BooleanSetting on = new BooleanSetting("On Target", false);

    public static int[] getScreenCoords(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new int[]{(int) screenCoords.get(0), (int) (Display.getHeight() - screenCoords.get(1)), (int) screenCoords.get(2)};
        }
        return null;
    }

    @EventTarget
    public void render(EventRender e) {
        if (on.get()) {
            wtf.clear();
            EntityPlayer player = (EntityPlayer) KillAura.target;
            double x;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * e.pt - mc.getRenderManager().viewerPosY;
            double z;
            x = player.lastTickPosX + ((player.posX + 10) - (player.lastTickPosX + 10)) * e.pt - mc.getRenderManager().viewerPosX;
            z = player.lastTickPosZ + ((player.posZ + 10) - (player.lastTickPosZ + 10)) * e.pt - mc.getRenderManager().viewerPosZ;
            y += player.height / 2f;
            int[] convertedPoints = getScreenCoords(x, y, z);
            float xd = Math.abs(getScreenCoords(x, y + 1.0D, z)[1] - getScreenCoords(x, y, z)[1]);
            assert convertedPoints != null;
            if ((convertedPoints[2] >= 0.0D) && (convertedPoints[2] < 1.0D)) {

                wtf.put(player, new float[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2]});
            }
        }

}
    public Drag targethud1 = Medusa.createDrag(this, "targethud", 300, 300);
    float hp;
    float ar;
    int posX;
    int posY;
    float size = 0;
    float status = 0;
    public Animation animation = new EaseBackIn(400, 1, 1.5f);
    EntityLivingBase target;
    @EventTarget
    public void onRender2d(EventDisplay eventRender2D) {
        if (!on.get() || !RenderUtil.isInViewFrustrum(KillAura.target)) {
            animation.setDuration(400);

            target = (KillAura.target instanceof EntityPlayer) ? KillAura.target : target;
            if (target == null) target = mc.player;

            if (KillAura.target != null || mc.currentScreen instanceof GuiChat) {
                if (KillAura.target == null) target = mc.player;
                animation.setDirection(Direction.FORWARDS);
            } else animation.setDirection(Direction.BACKWARDS);

            size = (float) animation.getOutput();

            if (size < 0.02) return;

            String name = ChatFormatting.stripFormatting(target.getName());
            float width = Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 100);
            targethud1.setWidth(width);
            targethud1.setHeight(35.5f);

            posX = (int) targethud1.getX();
            posY = (int) targethud1.getY();

            hp = MathHelper.clamp(MathHelper.interpolate(hp, target.getHealth() / target.getMaxHealth(), 0.8f), 0, 1);
            ar = MathHelper.clamp(MathHelper.interpolate(ar, target.getTotalArmorValue() / 20f, 0.8f), 0, 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(posX + width / 2, posY + 35.5 / 2, 0);
            GlStateManager.scale(size, size, 1);
            GlStateManager.translate(-posX - width / 2, -posY - 35.5 / 2, 0);
            RenderUtil.blur(() -> RoundedUtil.drawRound(posX, posY, width, 35.5f, 3, new Color(0, 0, 0, 255)), 15);

            RoundedUtil.drawRound(posX, posY, width, 35.5f,3, new Color(0, 0, 0, 100));

            RenderUtil.drawRectWH(posX + 2, posY + 2, 32, 32, Color.BLACK.getRGB());
            RenderUtil.drawFace(posX + 2, posY + 2, 8, 8, 8, 8, 32, 32, 64, 64, (AbstractClientPlayer) target);

            Fonts.BOLD18.drawString(name, posX + 28 + 8, posY + 4, -1);

//            Fonts.RUB14.drawString("Distance: " + MathHelper.round(target.getDistance(mc.player), 0.1f), posX + 28, posY + 20, -1);

//            RenderUtil.drawRectWH(posX + 1.5f, posY + 28.5f, width - 3, 2.5f, new Color(255, 255, 255, 64).getRGB());
            RoundedUtil.drawRound(posX + 4f + 32, posY + 26f, (width - 2f - 38 + 2), 8f,1, new Color(30, 30, 30));
            RoundedUtil.drawRound(posX + 4f + 33, posY + 27f, (width - 2f - 38) * hp, 6.0f,1, new Color(ClickGui.getColor()));
            Fonts.BOLD14.drawString("HP: " + MathHelper.round(target.getHealth(), 0.5f), posX + 28 + 10, posY + 28, -1);
//            RenderUtil.drawRectWH(posX + 1.5f, posY + 31.5f, width - 3, 2.5f, new Color(255, 255, 255, 63).getRGB());
//            RoundedUtil.drawRound(posX + 4f + 32, posY + 31f, (width - 2f - 36), 2.0f, 0, new Color(80, 80, 80));
//            RoundedUtil.drawRound(posX + 4f + 32, posY + 31f, (width - 2f - 36) * ar, 2.0f, 0, new Color(57, 213, 255));
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(posX, posY, width, 35.5f, 3,new Color(0,0,0,255));
            StencilUtil.readStencilBuffer(0);
            RenderUtil.drawBlurredShadow(posX, posY, width, 35.5f, 5, new Color(0,0,0, 255));
            StencilUtil.uninitStencilBuffer();
            GlStateManager.popMatrix();

        }
        else {

            if (mc.player != null && mc.world != null) {
                GlStateManager.pushMatrix();
                double twoDscale = ScaledResolution.getScaleFactor() / Math.pow(ScaledResolution.getScaleFactor(), 2.0);
                GlStateManager.scale(twoDscale, twoDscale, twoDscale);
                for (EntityPlayer player : wtf.keySet()) {
                    float[] renderPositions = wtf.get(player);
                    if ((renderPositions[2] >= 1.0D) || (renderPositions[3] <= 1.0D)) {
                        float posX = renderPositions[0];
                        float posY = renderPositions[1];
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(posX, posY, 0);
                        GlStateManager.scale(2, 2, 2);
                        GlStateManager.translate(-posX, -posY, 0);
                        String name = ChatFormatting.stripFormatting(player.getName());
                        float width = Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 80);
                        renderTarget((EntityLivingBase) player, (int) ((int) posX - width / 2f), (int) ((int) posY - 35.5f / 2f));

                        GlStateManager.popMatrix();

                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }



    public void renderTarget(EntityLivingBase target1, int x, int y) {
        EntityLivingBase target = (target1 instanceof EntityPlayer) ? target1 : mc.player;
        if (target == null || target == mc.player && !(mc.currentScreen instanceof GuiChat)) {
            hp = 0;
            ar = 0;
            return;
        }

        String name = ChatFormatting.stripFormatting(target.getName());
        float width = Math.max(Fonts.RUBM16.getStringWidth(name) + 50, 80);
        int posX = x;
        int posY = y;

        hp = MathHelper.interpolate(hp, target.getHealth() / target.getMaxHealth(), 0.8f);
        ar = MathHelper.interpolate(ar, target.getTotalArmorValue() / 20f, 0.8f);
        StencilUtil.initStencilToWrite();
        RenderUtil.drawRectWH(posX, posY, width, 35.5f, new Color(0, 0, 0, 128).getRGB());
        StencilUtil.readStencilBuffer(0);
        RenderUtil.drawBlurredShadow(posX, posY, width, 35.5f, 10, new Color(0, 0, 0, 200));
        StencilUtil.uninitStencilBuffer();

        RenderUtil.drawRectWH(posX, posY, width, 35.5f, new Color(0, 0, 0, 128).getRGB());

        RenderUtil.drawRectWH(posX + 2, posY + 2, 24, 24, Color.BLACK.getRGB());
        RenderUtil.drawFace(posX + 2, posY + 2, 8, 8, 8, 8, 24, 24, 64, 64, (AbstractClientPlayer) target);

        Fonts.RUBM16.drawString(name, posX + 28, posY + 4, -1);
        Fonts.RUB14.drawString("Health: " + MathHelper.round(target.getHealth(), 0.5f), posX + 28, posY + 12, -1);
        Fonts.RUB14.drawString("Distance: " + MathHelper.round(target.getDistance(mc.player), 0.1f), posX + 28, posY + 20, -1);

        RenderUtil.drawRectWH(posX + 1.5f, posY + 28.5f, width - 3, 2.5f, new Color(0, 0, 0, 64).getRGB());
        RenderUtil.horizontalGradient(posX + 2f, posY + 29f, posX + (width - 2f) * hp, posY + 29f + 1.5f, new Color(0, 156, 65).getRGB(), new Color(142, 255, 193).getRGB());

        RenderUtil.drawRectWH(posX + 1.5f, posY + 31.5f, width - 3, 2.5f, new Color(0, 0, 0, 63).getRGB());
        RenderUtil.horizontalGradient(posX + 2f, posY + 32f, posX + (width - 2f) * ar, posY + 32f + 1.5f, new Color(0, 103, 176).getRGB(), new Color(57, 213, 255).getRGB());
    }


}
