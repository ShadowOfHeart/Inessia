package gg.vape.module.impl.Render;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import gg.vape.Medusa;
import gg.vape.command.impl.CommandStaff;
import gg.vape.editor.Drag;
import gg.vape.event.EventTarget;
import gg.vape.event.events.impl.EventDisplay;
import gg.vape.event.events.impl.EventUpdate;
import gg.vape.helpers.SmartScissor;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;
import gg.vape.module.Category;
import gg.vape.module.Module;
import gg.vape.module.ModuleInfo;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.GameType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ModuleInfo(name = "StaffStatistic", type = Category.Hud)
public class StaffStatistic extends Module {

    public Drag staffListDrag = Medusa.createDrag(this, "staffListDrag", 5, 50);

    private static final Pattern validUserPattern = Pattern.compile("^\\w{3,16}$");
    List<String> players = new ArrayList<>();
    List<String> notSpec = new ArrayList<>();
    @EventTarget
    private final void onUpdate(EventUpdate e) {
        if (mc.player.ticksExisted % 10 == 0) {
            players = getVanish();
            notSpec = getOnlinePlayerD();
            players.sort(String::compareTo);
            notSpec.sort(String::compareTo);
        }
    };
    @EventTarget
    private final void onDraw(EventDisplay e) {

//        if (e.type == EventDraw.RenderType.DISPLAY) {
            if (players.isEmpty() && notSpec.isEmpty()) return;

            List<String> all = new ArrayList<>();
            all.addAll(players);
            all.addAll(notSpec);


            float width = all.stream().max(Comparator.comparingDouble(Fonts.BOLD14::getStringWidth)).map(Fonts.BOLD14::getStringWidth).get().floatValue() + 50;


            float height2 = (notSpec.size() + players.size()) * 14;

            float x = staffListDrag.getX();
            float y = staffListDrag.getY();

            staffListDrag.setWidth(width);
            staffListDrag.setHeight(height2 + 10);
            SmartScissor.push();
            SmartScissor.setFromComponentCoordinates((int) x - 3, (int) y - 3, (int) width + 6, (int) height2 + 12);


            RenderUtil.blur(() -> {
                RoundedUtil.drawRound(x, y, width, height2 + 8, 2, new Color(255, 255, 255, 80));
            }, 20);
            RoundedUtil.drawRoundOutline(x, y, width, height2 + 8,2, 0.5F, new Color(21, 21, 21, 150), new Color(0, 0, 0, 255));

            Fonts.BOLD16.drawString("Staff List ->", x + 3, y + 3, new Color(255, 255, 255, 255).getRGB());
            if (height2 > 10) {

                int staffY = 11;
                for (String player : all) {
                    Fonts.BOLD14.drawStringWithShadow(player.split(":")[0], x + 3 + 31, y + 4 + staffY, -1);
                    Fonts.BOLD16.drawStringWithShadow(player.split(":")[1].equalsIgnoreCase("vanish")
                            ? ChatFormatting.RED + "[SPEC]" : player.split(":")[1].equalsIgnoreCase("gm3")//[A]
                            ? ChatFormatting.RED + "[SPEC]" + ChatFormatting.YELLOW /*+ "(GM 3)"*/ : ChatFormatting.GREEN + "[ACTIV]", x + 3, y + staffY + 4 - 1, new Color(255, 255, 255, 255).getRGB());
                    staffY += 13;
                }
            }
            SmartScissor.unset();
            SmartScissor.pop();

//        }
    };

    public static List<String> getOnlinePlayer() {
        return mc.player.connection.getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .map(GameProfile::getName)
                .filter(profileName -> validUserPattern.matcher(profileName).matches())
                .collect(Collectors.toList());
    }


    public static List<String> getOnlinePlayerD() {
        List<String> S = new ArrayList<>();
        for (NetworkPlayerInfo player : mc.player.connection.getPlayerInfoMap()) {
            if (mc.isSingleplayer() || player.getPlayerTeam() == null) break;
            String prefix = player.getPlayerTeam().getPrefix();

            if (check(ChatFormatting.stripFormatting(prefix).toLowerCase())
                    || CommandStaff.staffNames.toString().toLowerCase().contains(player.getGameProfile().getName().toLowerCase())
                    || player.getGameProfile().getName().toLowerCase().contains("1danil_mansoru1") || player.getPlayerTeam().getPrefix().contains("YT")) {
                String name = Arrays.asList(player.getPlayerTeam().getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");

                if (player.getGameType() == GameType.SPECTATOR) {
                    S.add(player.getPlayerTeam().getPrefix() + name + ":gm3");
                    continue;
                }
                S.add(player.getPlayerTeam().getPrefix() + name + ":active");
            }
        }
        return S;
    }

    public List<String> getVanish() {
        List<String> list = new ArrayList<>();
        for (ScorePlayerTeam s : mc.world.getScoreboard().getTeams()) {
            if (s.getPrefix().length() == 0 || mc.isSingleplayer()) continue;
            String name = Arrays.asList(s.getMembershipCollection().stream().toArray()).toString().replace("[", "").replace("]", "");

            if (getOnlinePlayer().contains(name) || name.isEmpty())
                continue;
            if (CommandStaff.staffNames.toString().toLowerCase().contains(name.toLowerCase())
                    && check(s.getPrefix().toLowerCase())
                    || check(s.getPrefix().toLowerCase())
                    || name.toLowerCase().contains("1danil_mansoru1") || s.getPrefix().contains("YT"))
                list.add(s.getPrefix() + name + ":vanish");
        }
        return list;
    }

    public static boolean check(String name) {
        return name.contains("helper") || name.contains("moder") || name.contains("admin") || name.contains("owner") || name.contains("curator") || name.contains("??????") || name.contains("?????") || name.contains("?????") || name.contains("???????");
    }

}
