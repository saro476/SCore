package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.utils.logging.Utils;
import com.ssomar.score.SCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BungeeSend extends MixedCommand {

    @Override
    public void run(Player p, LivingEntity receiver, List<String> args, ActionInfo aInfo) {
        
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            SCore.plugin.getServer().getLogger().info("SCore: Sending " + p.getName() + " to " + args.get(1));
            out.writeUTF("ConnectOther");
            out.writeUTF(p.getName());
            out.writeUTF(args.get(1));
        } catch(Exception e) {
            e.printStackTrace();
        }

        p.sendPluginMessage( SCore.plugin, "BungeeCord", b.toByteArray());
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        if (args.size() < 1) return Optional.of(notEnoughArgs + getTemplate());
        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("BUNGEESEND");
        return names;
    }

    @Override
    public String getTemplate() {
        return "BUNGEESEND {server}";
    }

    @Override
    public ChatColor getColor() {
        return null;
    }

    @Override
    public ChatColor getExtraColor() {
        return null;
    }
}
