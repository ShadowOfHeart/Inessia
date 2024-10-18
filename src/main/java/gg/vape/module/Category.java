package gg.vape.module;

import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.impl.EaseInOutQuad;

public enum Category {
    Combat, Movement, Render, Misc, Player, Hud;
    public Animation animation = new EaseInOutQuad(500, 1);
}
