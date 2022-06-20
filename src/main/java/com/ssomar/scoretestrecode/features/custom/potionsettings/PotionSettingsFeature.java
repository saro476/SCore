package com.ssomar.scoretestrecode.features.custom.potionsettings;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.scoretestrecode.editor.NewGUIManager;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.potioneffects.group.PotionEffectGroupFeature;
import com.ssomar.scoretestrecode.features.custom.potioneffects.potioneffect.PotionEffectFeature;
import com.ssomar.scoretestrecode.features.types.BooleanFeature;
import com.ssomar.scoretestrecode.features.types.ColorIntegerFeature;
import com.ssomar.scoretestrecode.features.types.PotionTypeFeature;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter @Setter
public class PotionSettingsFeature extends FeatureWithHisOwnEditor<PotionSettingsFeature, PotionSettingsFeature, PotionSettingsFeatureEditor, PotionSettingsFeatureEditorManager> {

    private ColorIntegerFeature color;
    private PotionTypeFeature potiontype;
    private BooleanFeature potionExtended;
    private BooleanFeature potionUpgraded;
    private PotionEffectGroupFeature potionEffects;

    public PotionSettingsFeature(FeatureParentInterface parent) {
        super(parent, "potionSettings", "Potion Settings", new String[]{"&7&oPotion settings"}, Material.BREWING_STAND, false);
        reset();
    }

    @Override
    public void reset() {
        this.color = new ColorIntegerFeature(this, "potionColor", Optional.ofNullable(255), "Potion color", new String[]{"&7&oColor"}, GUI.ORANGE, false);
        this.potiontype = new PotionTypeFeature(this, "potionType", Optional.ofNullable(PotionType.WATER), "Potion type", new String[]{"&7&oPotion type"}, Material.POTION, false);
        this.potionExtended = new BooleanFeature(this, "potionExtended", false, "Potion extended", new String[]{"&7&oPotion extended"}, Material.LEVER, false, false);
        this.potionUpgraded = new BooleanFeature(this, "potionUpgraded", false, "Potion upgraded", new String[]{"&7&oPotion upgraded"}, Material.LEVER, false, false);
        this.potionEffects = new PotionEffectGroupFeature(this);
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        errors.addAll(this.color.load(plugin, config, isPremiumLoading));
        errors.addAll(this.potiontype.load(plugin, config, isPremiumLoading));
        errors.addAll(this.potionExtended.load(plugin, config, isPremiumLoading));
        errors.addAll(this.potionUpgraded.load(plugin, config, isPremiumLoading));
        errors.addAll(this.potionEffects.load(plugin, config, isPremiumLoading));

        return errors;
    }

    public List<String> load(SPlugin plugin, ItemStack item, boolean isPremiumLoading) {
        ItemMeta meta;
        if (item.hasItemMeta() && (meta = item.getItemMeta()) instanceof PotionMeta) {
            PotionMeta pMeta = ((PotionMeta) meta);
            if (!SCore.is1v11Less()) {
                color.setValue(Optional.of(pMeta.getColor().asRGB()));
                potiontype.setValue(Optional.of(pMeta.getBasePotionData().getType()));
                potionExtended.setValue(pMeta.getBasePotionData().isExtended());
                potionUpgraded.setValue(pMeta.getBasePotionData().isUpgraded());
            }

            int i = 0;
            for (PotionEffect pE : pMeta.getCustomEffects()) {
                PotionEffectFeature pEF = new PotionEffectFeature(potionEffects, "pEffect" + i);
                pEF.getType().setValue(Optional.of(pE.getType()));
                pEF.getAmplifier().setValue(Optional.of(pE.getAmplifier()));
                pEF.getDuration().setValue(Optional.of(pE.getDuration()));
                pEF.getAmbient().setValue(pE.isAmbient());
                pEF.getParticles().setValue(pE.hasParticles());
                pEF.getIcon().setValue(pE.hasIcon());
                potionEffects.getAttributes().put("pEffect" + i, pEF);
                i++;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void save(ConfigurationSection config) {
        color.save(config);
        potiontype.save(config);
        potionExtended.save(config);
        potionUpgraded.save(config);
        potionEffects.save(config);
    }

    @Override
    public PotionSettingsFeature getValue() {
        return this;
    }

    @Override
    public PotionSettingsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 6];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 6] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 5] = "&7Potion color : &e" + color.getValue().get();
        finalDescription[finalDescription.length - 4] = "&7Potion type : &e" + potiontype.getValue().get();
        if(potionExtended.getValue())
            finalDescription[finalDescription.length - 3] = "&7Potion extended: &a&l✔";
        else
            finalDescription[finalDescription.length - 3] = "&7Potion extended: &c&l✘";
        if(potionUpgraded.getValue())
            finalDescription[finalDescription.length - 2] = "&7Potion upgraded: &a&l✔";
        else
            finalDescription[finalDescription.length - 2] = "&7Potion upgraded: &c&l✘";

        finalDescription[finalDescription.length - 1] = "&7Potion effects: &e" + potionEffects.getValue().getAttributes().size();

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public void extractInfoFromParentEditor(NewGUIManager manager, Player player) {

    }

    @Override
    public PotionSettingsFeature clone() {
        PotionSettingsFeature dropFeatures = new PotionSettingsFeature(getParent());
        dropFeatures.setColor(getColor().clone());
        dropFeatures.setPotiontype(getPotiontype().clone());
        dropFeatures.setPotionExtended(getPotionExtended().clone());
        dropFeatures.setPotionUpgraded(getPotionUpgraded().clone());
        dropFeatures.setPotionEffects(getPotionEffects().clone());
        return dropFeatures;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(Arrays.asList(color, potiontype, potionExtended, potionUpgraded, potionEffects));
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return getParent().getConfigurationSection();
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for(FeatureInterface feature : getParent().getFeatures()) {
            if(feature instanceof PotionSettingsFeature) {
                PotionSettingsFeature hiders = (PotionSettingsFeature) feature;
                hiders.setColor(getColor());
                hiders.setPotiontype(getPotiontype());
                hiders.setPotionExtended(getPotionExtended());
                hiders.setPotionUpgraded(getPotionUpgraded());
                hiders.setPotionEffects(getPotionEffects());
                break;
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        PotionSettingsFeatureEditorManager.getInstance().startEditing(player, this);
    }

}