package gg.vape.module;

import gg.vape.helpers.font.Fonts;
import gg.vape.module.impl.Combat.*;
import gg.vape.module.impl.Misc.*;
import gg.vape.module.impl.Movement.*;
import gg.vape.module.impl.Player.*;
import gg.vape.module.impl.Render.*;


import java.util.ArrayList;

public class Manager {
    public ArrayList<Module> m = new ArrayList<>();

    public Manager() {
        //COMBAT
        m.add(new AntiBot());
        m.add(new BowAimBot());
        m.add(new Velocity());


        //MISC
        m.add(new BeaconExploit());
        m.add(new ItemSwapFix());
        m.add(new PasswordHider());
        m.add(new XrayBypass());

        //MOVEMENT
        m.add(new Flight());
        m.add(new GuiWalk());
        m.add(new Jesus());
        m.add(new NoClip());
        m.add(new NoSlow());
        m.add(new Speed());
        m.add(new Spider());
        m.add(new Sprint());
        m.add(new Strafe());
        m.add(new NoPush());
        m.add(new NoWaterCollision());
        m.add(new HighJump());
        m.add(new LongJump());

        //PLAYER
        m.add(new AutoRespawn());
        m.add(new MiddleClickPearl());
        m.add(new ItemScroller());
        m.add(new NoDelay());
        m.add(new Timer());
        m.add(new AutoFish());
        m.add(new XCarry());
        m.add(new NoRotationSet());
        m.add(new AntiLagMachine());
        m.add(new Baritone());
        m.add(new Freecam());
        m.add(new GAppleTimer());
        m.add(new AutoLeave());
        m.add(new RotationView());
        m.add(new Optimizer());
        //RENDER

        m.add(new ExtraTab());
//        m.add(new ShaderESP());
        m.add(new ViewModel());
        m.add(new MiddleClickFriend());
        m.add(new Chams());
        m.add(new CameraClip());
        m.add(new SwingAnimation());
        m.add(new NameTag());
        m.add(new JumpCircle());
        m.add(new ClickGui());
        m.add(new FeatureList());

        m.add(new FullBright());
        m.add(new NoRender());
        m.add(new Predict());
        m.add(new Watermark());

//        m.add(new WorldColor());
        m.add(new WorldTime());
//        m.add(new ItemESP());
        m.add(new ClientSound());
        m.add(new ESP());
        m.add(new LowHPScreen());
//        m.add(new ChinaHat());
        m.add(new Exploit());
        m.add(new AirJump());
        m.add(new StaffStatistic());
//        m.add(new WaterSpeed());
        m.add(new PotionHUD());
        m.add(new NoScoreboard());
        m.add(new TriangleESP());

        m.add(new DamageParticles());
        m.add(new TargetHud());

        //        m.sort((o1, o2) -> o1.getSettings().size() >= o2.getSettings().size() ? 1 : -1);
        m.sort((f1, f2) -> Fonts.BOLD14.getStringWidth(f1.getDisplayName()) > Fonts.BOLD14.getStringWidth(f2.getDisplayName()) ? -1 : 1);
    }

    public Module getModule(String name) {
        for (Module m : this.m) {
            if (m.name.equals(name)) {
                return m;
            }
        }
        return null;
    }


    public ArrayList<Module> getModulesFromCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module m : this.m) {
            if (m.category == category) {
                modules.add(m);
            }
        }
        return modules;
    }

    public Module getModule(Class<?> clazz) {
        for (Module m : this.m) {
            if (m.getClass() == clazz) {
                return m;
            }
        }
        return null;
    }


}