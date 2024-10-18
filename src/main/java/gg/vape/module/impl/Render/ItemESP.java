package gg.vape.module.impl.Render;

import gg.vape.helpers.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventRender;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "ItemESP", type = Category.Render)
public class ItemESP extends Module {

    @EventTarget
    public void onRender(EventRender e) {
        for (EntityItem item : mc.world.getEntities(EntityItem.class, Entity::isEntityAlive)) {
            RenderUtil.renderItem(item, new Color(255, 255, 255, 100), e.pt);
        }
    }

}
