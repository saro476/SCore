package com.ssomar.score.commands.runnable.block.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.RunConsoleCommand;
import com.ssomar.score.commands.runnable.block.BlockCommand;
import com.ssomar.score.usedapi.WorldGuardAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetBlockPos extends BlockCommand {

    @Override
    public void run(@Nullable Player p, @NotNull Block block, Material oldMaterial, List<String> args, ActionInfo aInfo) {
        try {
            int x = Integer.valueOf(args.get(0));
            int y = Integer.valueOf(args.get(1));
            int z = Integer.valueOf(args.get(2));

            String mat = args.get(3).toUpperCase();

            boolean bypassWG = false;
            if(args.size() >= 5) bypassWG = Boolean.parseBoolean(args.get(4));

            Location loc = new Location(block.getWorld(), x, y, z);

            if(Material.matchMaterial(mat) != null) {
                if(SCore.hasWorldGuard && p != null && !bypassWG) {
                    if(new WorldGuardAPI().canBuild(p, loc)) {
                        loc.getBlock().setType(Material.valueOf(mat));
                    }
                }
                else {
                    loc.getBlock().setType(Material.valueOf(mat));
                }
            }else {
                World w = block.getWorld();
                List<Entity> entities = w.getEntities();

                if(entities.size() > 0)
                    RunConsoleCommand.runConsoleCommand("execute at "+entities.get(0).getUniqueId()+" run setblock "+x+" "+y+" "+z+" "+args.get(0).toLowerCase()+" replace", aInfo.isSilenceOutput());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public String verify(List<String> args) {
        String error = "";
        /* Delete verification to not interfer with the vanilla setblock command */
//		String setblock = "SETBLOCK {material}";
//		if(args.size()<1) error = notEnoughArgs+setblock;
//		else if(args.size()>1)error = tooManyArgs+setblock;

        return error;
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("SETBLOCKPOS");
        return names;
    }

    @Override
    public String getTemplate() {
        return "SETBLOCKPOS {x} {y} {z} {material} [bypassWG true or false]";
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

