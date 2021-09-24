package com.ssomar.score.projectiles.features;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VisualItemFeature extends DecorateurCustomProjectiles {

    ItemStack item;
    boolean hasItem;

    public VisualItemFeature(CustomProjectile cProj){
        super(cProj.getId(), cProj.getProjConfig());
        super.cProj = cProj;
        item = null;
        this.hasItem = false;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        if (projConfig.contains("visualItem")) {
            String material = projConfig.getString("visualItem", "");
            try {
                item = new ItemStack(Material.valueOf(material.toUpperCase()));
                if (projConfig.contains("customModelData")) {
                    ItemMeta meta = item.getItemMeta();
                    meta.setCustomModelData(projConfig.getInt("customModelData", 0));
                    item.setItemMeta(meta);
                }
                hasItem = true;
            } catch (Exception e) {
            }
        }
        return cProj.loadConfiguration() && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (!SCore.is1v12() && e instanceof ThrowableProjectile && hasItem) {
            ((ThrowableProjectile) e).setItem(item);
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        gui.addItem(Material.ITEM_FRAME, 1, gui.TITLE_COLOR+"Visual item", false, false, gui.CLICK_HERE_TO_CHANGE, "&7actually: ");
        if(item == null) gui.updateActually(gui.TITLE_COLOR+"Visual item", "VANILLA ITEM");
        else gui.updateActually(gui.TITLE_COLOR+"Visual item", item.getType().toString());
        return gui;
    }
}