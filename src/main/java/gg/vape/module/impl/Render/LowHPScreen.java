package gg.vape.module.impl.Render;

import com.viaversion.viaversion.util.MathUtil;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

@ModuleInfo(name = "LowHPScreen", type = Category.Render)
public class LowHPScreen extends Module {





    int dynamic_alpha = 0;
    int nuyahz = 0;

    @EventTarget
    public void onRender2D(EventDisplay e){

        Color color2 = new Color(255,0,0, MathUtil.clamp(dynamic_alpha + 40,0,255));

        if(mc.player.getHealth() < 10) {
            ScaledResolution sr = new ScaledResolution(mc);
            RenderUtil.draw2DGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledWidth(), color2.getRGB(), new Color(0,0,0,0).getRGB(),  color2.getRGB(), new Color(0,0,0,0).getRGB());
            if(mc.player.getHealth() > 9){
                nuyahz = 18;
            } else
            if(mc.player.getHealth() > 8){
                nuyahz = 36;
            } else
            if(mc.player.getHealth() > 7){
                nuyahz = 54;
            } else
            if(mc.player.getHealth() > 6){
                nuyahz = 72;
            } else
            if(mc.player.getHealth() > 5){
                nuyahz = 90;
            } else
            if(mc.player.getHealth() > 4){
                nuyahz = 108;
            } else
            if(mc.player.getHealth() > 3){
                nuyahz = 126;
            } else
            if(mc.player.getHealth() > 2){
                nuyahz = 144;
            } else
            if(mc.player.getHealth() > 1){
                nuyahz = 162;
            } else
            if(mc.player.getHealth() > 0){
                nuyahz = 180;
            }
        }

        if(nuyahz > dynamic_alpha){
            dynamic_alpha = dynamic_alpha + 3;
        }
        if(nuyahz < dynamic_alpha){
            dynamic_alpha = dynamic_alpha - 3;
        }

    }

    //dalpha 180/10
    // 18

}
