package gg.vape.clickgui.panels;

import gg.vape.Medusa;
import gg.vape.clickgui.panels.components.Component;
import net.minecraft.client.gui.Gui;
import gg.vape.clickgui.panels.components.ModuleComponent;
import gg.vape.helpers.StencilUtil;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.module.Category;
import gg.vape.module.Module;

import java.awt.*;
import java.util.ArrayList;

public class Panel {

    public Category category;
    public float x, y;
    public ArrayList<gg.vape.clickgui.panels.components.Component> components = new ArrayList<>();
    public float height;

    public Panel(Category category, float x, float y) {
        this.category = category;
        this.x = x;
        this.y = y;
        int yOffset = 20;
        for (Module module : Medusa.m.getModulesFromCategory(category)) {
            components.add(new ModuleComponent(module, this, x, y + yOffset, 125, 16));
            yOffset+=16;
        }

    }




    public void render(int mouseX, int mouseY) {
        for (gg.vape.clickgui.panels.components.Component component : components) {
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                if (moduleComponent.opened) {
                    height = moduleComponent.offset + 20;
                }
            }
        }
        RenderUtil.blur(() -> {
            RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(20, 20, 20, 150));
        }, 10);
        RenderUtil.bloom(() -> {
            RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(20, 20, 20, 150));
        }, 5, 2, 1);
        RoundedUtil.drawRound(x, y, 125, getHeight(), 5, new Color(10, 10, 10, 220));
        Fonts.RUB14.drawCenteredString(category.name(), x + 125 / 2f, y + 7, -1);
        Gui.drawGradientRect(x, y + 17, x + 125, y + 25, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, 125,getHeight(), 5, new Color(10, 10, 10, 180));
        StencilUtil.readStencilBuffer(1);
        components.forEach(component -> {
            component.render(mouseX, mouseY);
        });
        StencilUtil.uninitStencilBuffer();

    }

    public float getHeight() {
        float height = 20;
        for (Component component : components) {
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                if (moduleComponent.opened) {
                    height = moduleComponent.offset + 20;
                }
            }
        }
        return height;
    }

    public void click(int mouseX, int mouseY, int button) {
        components.forEach(component -> {
                if (RenderUtil.isHovered(mouseX, mouseY, component.x, component.y, component.width, component.height)) {
                    component.click(mouseX, mouseY, button);
                }
        });
    }

}
