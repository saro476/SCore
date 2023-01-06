package com.ssomar.score.commands.runnable.block.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.block.BlockCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/* LAUNCH {projectileType} */
@SuppressWarnings("deprecation")
public class Move extends BlockCommand {

    @Override
    public void run(Player p, @NotNull Block block, Material oldMaterial, List<String> args, ActionInfo aInfo) {

        BlockData data;
        Directional directional;
        if ((data = block.getBlockData()) == null || !(data instanceof Directional)) return;

        directional = (Directional) data;

        Location bLoc = block.getLocation();

        Collection<Entity> entities = bLoc.getWorld().getNearbyEntities(bLoc, 1, 1.5, 1);
        for (Entity entity : entities) {
            final Entity e = entity;
            for (int i = 0; i < 15; i++) {
                final long delay = i;
                BukkitRunnable runnable = new BukkitRunnable() {
                    public void run() {

                        boolean isItem = e instanceof Item;


                        if (e.getLocation().subtract(0, 0.1, 0).getBlock().getLocation().equals(bLoc)
                                || e.getLocation().subtract(0, 0.8, 0).getBlock().getLocation().equals(bLoc)) {

                            //SsomarDev.testMsg("move >> " + e.getType().name(), true);

                            Vector direction = directional.getFacing().getDirection();

                            Location eLoc = e.getLocation();
                            // recenter the entity on the center f the block
                            double x = eLoc.getX() - (int) eLoc.getX();
                            double z = eLoc.getZ() - (int) eLoc.getZ();
                            if (x < 0.5 && x >= 0) x = 0.5 - x;
                            else if (x > 0.5) x = -(x - 0.5);
                            else if (x < 0 && x >= -0.5) x = -(0.5 + x);
                            else if (x < -0.5) x = -(0.5 + x);

                            if (z < 0.5 && z >= 0) z = 0.5 - z;
                            else if (z > 0.5) z = -(z - 0.5);
                            else if (z < 0 && z >= -0.5) z = -(0.5 + z);
                            else if (z < -0.5) z = -(0.5 + z);

                            // avoid back velocity
                            if (direction.getX() > 0 && x < 0) x = 0;
                            else if (direction.getX() < 0 && x > 0) x = 0;
                            if (direction.getZ() > 0 && z < 0) z = 0;
                            else if (direction.getZ() < 0 && z > 0) z = 0;

                            double y = 0;
                            // to make items go up on stairs
                            if (isItem) y = 0.8;

                            Vector centerBlock = new Vector(x, y, z);
                            //SsomarDev.testMsg("centerBlock >> " + centerBlock.toString(), true);
                            // e.setVelocity(centerBlock);
                            e.setVelocity(direction.add(centerBlock).multiply(0.1));
                    /* BukkitRunnable runnable = new BukkitRunnable() {
                        public void run() {
                            e.setVelocity(direction.multiply(0.2));
                        }
                    };
                    runnable.runTaskLaterAsynchronously(SCore.plugin, 1);*/

                        } else cancel();
                    }
                };
                runnable.runTaskLaterAsynchronously(SCore.plugin, delay);
            }


        }

    }


    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";
        String launch = "MOVE";
        if (args.size() < 0) error = notEnoughArgs + launch;

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("MOVE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "MOVE";
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
