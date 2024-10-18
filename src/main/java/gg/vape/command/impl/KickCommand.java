package gg.vape.command.impl;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@CommandInfo(name = "kick")
public class KickCommand extends Command {
    public static InetAddress inetaddress;
    @Override
    public void execute(String[] args) {
        super.execute(args);
        new Thread(() -> {
            String ip = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostAddress();
            String ip2 = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostName();
            int port = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getPort();

            try {
                inetaddress = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            if(inetaddress != null && ip != null && ip2 != null && port != 0) {
                GuiConnecting.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, (Minecraft.getMinecraft()).gameSettings.isUsingNativeTransport());
                GuiConnecting.networkManager.setNetHandler(new NetHandlerHandshakeTCP(mc.player.getServer(), GuiConnecting.networkManager));
                GuiConnecting.networkManager.sendPacket(new C00Handshake(ip2, port, EnumConnectionState.LOGIN));
                GuiConnecting.networkManager.sendPacket(new CPacketLoginStart(new GameProfile(null, args[1])));
                ChatUtil.print("Kicked " + args[1]);
            } else {
                ChatUtil.print("Error");
            }
        }).start();

    }
}
