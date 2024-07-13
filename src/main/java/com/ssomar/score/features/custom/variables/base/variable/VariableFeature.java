package com.ssomar.score.features.custom.variables.base.variable;

import com.ssomar.score.features.FeatureInterface;
import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureSettingsSCore;
import com.ssomar.score.features.FeatureWithHisOwnEditor;
import com.ssomar.score.features.types.ColoredStringFeature;
import com.ssomar.score.features.types.DoubleFeature;
import com.ssomar.score.features.types.UncoloredStringFeature;
import com.ssomar.score.features.types.VariableTypeFeature;
import com.ssomar.score.features.types.list.ListColoredStringFeature;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.emums.VariableType;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class VariableFeature<T> extends FeatureWithHisOwnEditor<VariableFeature, VariableFeature<T>, VariableFeatureEditor, VariableFeatureEditorManager> {

    private UncoloredStringFeature variableName;
    private VariableTypeFeature type;
    private ColoredStringFeature stringValue;
    private DoubleFeature doubleValue;
    private ListColoredStringFeature listValue;
    private String id;

    public VariableFeature(FeatureParentInterface parent, String id) {
        super(parent, FeatureSettingsSCore.variable);
        this.id = id;
        reset();
    }

    @Override
    public void reset() {
        this.variableName = new UncoloredStringFeature(this, Optional.of("var"), FeatureSettingsSCore.variableName, false);
        this.type = new VariableTypeFeature(this, Optional.of(VariableType.STRING), FeatureSettingsSCore.type, false);
        this.stringValue = new ColoredStringFeature(this, Optional.of(""), FeatureSettingsSCore.default_string, false);
        this.doubleValue = new DoubleFeature(this, Optional.of(0.0), FeatureSettingsSCore.default_double);
        this.listValue = new ListColoredStringFeature(this,  new ArrayList<>(), FeatureSettingsSCore.default_list, false, Optional.empty());
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        if (config.isConfigurationSection(id)) {
            ConfigurationSection enchantmentConfig = config.getConfigurationSection(id);
            errors.addAll(this.variableName.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.type.load(plugin, enchantmentConfig, isPremiumLoading));
            if (type.getValue().get().equals(VariableType.STRING)) {
                errors.addAll(this.stringValue.load(plugin, enchantmentConfig, isPremiumLoading));
            } else if (type.getValue().get().equals(VariableType.LIST)) {
                errors.addAll(this.listValue.load(plugin, enchantmentConfig, isPremiumLoading));
            } else {
                errors.addAll(this.doubleValue.load(plugin, enchantmentConfig, isPremiumLoading));
            }
        } else {
            errors.add("&cERROR, Couldn't load the Variable with its options because there is not section with the good ID: " + id + " &7&o" + getParent().getParentInfo());
        }
        return errors;
    }

    public T getDefaultValue() {
        if (type.getValue().get().equals(VariableType.STRING)) {
            return (T) stringValue.getValue().get();
        } else if (type.getValue().get().equals(VariableType.LIST)) {
            return (T) listValue.getValue();
        }
        else {
            return (T) doubleValue.getValue(null, new StringPlaceholder()).get();
        }
    }

    @Override
    public boolean isTheFeatureClickedParentEditor(String featureClicked) {
        return featureClicked.contains(getEditorName()) && featureClicked.contains("(" + id + ")");
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(id, null);
        ConfigurationSection attributeConfig = config.createSection(id);
        this.variableName.save(attributeConfig);
        this.type.save(attributeConfig);
        if (type.getValue().get().equals(VariableType.STRING)) {
            this.stringValue.save(attributeConfig);
        } else if (type.getValue().get().equals(VariableType.LIST)) {
            this.listValue.save(attributeConfig);
        }
        else {
            this.doubleValue.save(attributeConfig);
        }
    }

    @Override
    public VariableFeature getValue() {
        return this;
    }

    @Override
    public VariableFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 4];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 4] = "&7Name: &e" + variableName.getValue().get();
        finalDescription[finalDescription.length - 3] = "&7Type: &e" + type.getValue().get();
        if (type.getValue().get().equals(VariableType.STRING))
            finalDescription[finalDescription.length - 2] = "&7Default: &e" + stringValue.getValue().get();
        else if (type.getValue().get().equals(VariableType.LIST))
            finalDescription[finalDescription.length - 2] = "&7Default: &e" + Arrays.toString(listValue.getValue().toArray());
        else finalDescription[finalDescription.length - 2] = "&7Default: &e" + doubleValue.getValue().get();
        finalDescription[finalDescription.length - 1] = GUI.CLICK_HERE_TO_CHANGE;

        gui.createItem(getEditorMaterial(), 1, slot, GUI.TITLE_COLOR + getEditorName() + " - " + "(" + id + ")", false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public VariableFeature clone(FeatureParentInterface parent) {
        VariableFeature eF = new VariableFeature(parent, id);
        eF.setVariableName(variableName.clone(eF));
        eF.setType(type.clone(eF));
        eF.setStringValue(stringValue.clone(eF));
        eF.setDoubleValue(doubleValue.clone(eF));
        eF.setListValue(listValue.clone(eF));
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(Arrays.asList(variableName, type, stringValue, doubleValue, listValue));
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
        for (FeatureInterface feature : getParent().getFeatures()) {
            if (feature instanceof VariableFeature) {
                VariableFeature aFOF = (VariableFeature) feature;
                if (aFOF.getId().equals(id)) {
                    aFOF.setVariableName(variableName);
                    aFOF.setType(type);
                    aFOF.setStringValue(stringValue);
                    aFOF.setDoubleValue(doubleValue);
                    aFOF.setListValue(listValue);
                    break;
                }
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        VariableFeatureEditorManager.getInstance().startEditing(player, this);
    }

}
