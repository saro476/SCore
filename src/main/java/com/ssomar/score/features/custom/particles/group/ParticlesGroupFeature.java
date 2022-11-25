package com.ssomar.score.features.custom.particles.group;

import com.ssomar.score.SCore;
import com.ssomar.score.features.FeatureInterface;
import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureWithHisOwnEditor;
import com.ssomar.score.features.FeaturesGroup;
import com.ssomar.score.features.custom.particles.particle.ParticleFeature;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.projectiles.features.SProjectileFeatureInterface;
import com.ssomar.score.splugin.SPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.color.RegularColor;
import xyz.xenondevs.particle.data.texture.BlockTexture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ParticlesGroupFeature extends FeatureWithHisOwnEditor<ParticlesGroupFeature, ParticlesGroupFeature, ParticlesGroupFeatureEditor, ParticlesGroupFeatureEditorManager> implements FeaturesGroup<ParticleFeature>, SProjectileFeatureInterface {

    private Map<String, ParticleFeature> particles;
    private boolean notSaveIfNoValue;

    public ParticlesGroupFeature(FeatureParentInterface parent, boolean notSaveIfNoValue) {
        super(parent, "particles", "Particles", new String[]{"&7&oThe particles"}, Material.BLAZE_POWDER, false);
        this.notSaveIfNoValue = notSaveIfNoValue;
        reset();
    }

    @Override
    public void reset() {
        this.particles = new HashMap<>();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(this.getName())) {
            ConfigurationSection enchantmentsSection = config.getConfigurationSection(this.getName());
            for (String enchantmentID : enchantmentsSection.getKeys(false)) {
                ParticleFeature enchantment = new ParticleFeature(this, enchantmentID);
                List<String> subErrors = enchantment.load(plugin, enchantmentsSection, isPremiumLoading);
                if (subErrors.size() > 0) {
                    error.addAll(subErrors);
                    continue;
                }
                particles.put(enchantmentID, enchantment);
            }
        }
        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(this.getName(), null);
        if (notSaveIfNoValue && particles.size() == 0) return;
        ConfigurationSection enchantmentsSection = config.createSection(this.getName());
        for (String enchantmentID : particles.keySet()) {
            particles.get(enchantmentID).save(enchantmentsSection);
        }

    }

    @Override
    public ParticlesGroupFeature getValue() {
        return this;
    }

    @Override
    public ParticlesGroupFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = GUI.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7&oParticle(s) added: &e" + particles.size();

        gui.createItem(getEditorMaterial(), 1, slot, GUI.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public ParticleFeature getTheChildFeatureClickedParentEditor(String featureClicked) {
        for (ParticleFeature x : particles.values()) {
            if (x.isTheFeatureClickedParentEditor(featureClicked)) return x;
        }
        return null;
    }

    @Override
    public ParticlesGroupFeature clone(FeatureParentInterface newParent) {
        ParticlesGroupFeature eF = new ParticlesGroupFeature(newParent, isNotSaveIfNoValue());
        HashMap<String, ParticleFeature> newEnchantments = new HashMap<>();
        for (String x : particles.keySet()) {
            newEnchantments.put(x, particles.get(x).clone(eF));
        }
        eF.setParticles(newEnchantments);
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(particles.values());
    }

    @Override
    public String getParentInfo() {
        if (getParent() == this) {
            return "";
        } else return getParent().getParentInfo() + ".(" + getName() + ")";
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        ConfigurationSection section = getParent().getConfigurationSection();
        if (section.isConfigurationSection(this.getName())) {
            return section.getConfigurationSection(this.getName());
        } else return section.createSection(this.getName());
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : getParent().getFeatures()) {
            if (feature instanceof ParticlesGroupFeature) {
                ParticlesGroupFeature eF = (ParticlesGroupFeature) feature;
                eF.setParticles(this.getParticles());
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
        ParticlesGroupFeatureEditorManager.getInstance().startEditing(player, this);
    }

    @Override
    public void createNewFeature(@NotNull Player editor) {
        String baseId = "particle";
        for (int i = 0; i < 1000; i++) {
            String id = baseId + i;
            if (!particles.containsKey(id)) {
                ParticleFeature eF = new ParticleFeature(this, id);
                particles.put(id, eF);
                eF.openEditor(editor);
                break;
            }
        }
    }

    @Override
    public void deleteFeature(@NotNull Player editor, ParticleFeature feature) {
        particles.remove(feature.getId());
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher, Material materialLaunched) {
        if (particles != null) {
            for (ParticleFeature particle : particles.values()) {
                /* BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.isDead()) cancel();

                        if (particle.canHaveRedstoneColor()) {
                            Particle.DustOptions dO = new Particle.DustOptions(Color.RED, 1);
                            if (particle.getRedstoneColor().getValue().isPresent())
                                dO = new Particle.DustOptions(particle.getRedstoneColor().getValue().get(), 1);
                            e.getWorld().spawnParticle(particle.getParticlesType().getValue().get(), e.getLocation(), particle.getParticlesAmount().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesSpeed().getValue().get(), dO);
                        } else if (particle.canHaveBlocktype()) {
                            BlockData typeData = Material.STONE.createBlockData();
                            if (particle.getBlockType() != null)
                                typeData = particle.getBlockType().getValue().get().createBlockData();
                            e.getWorld().spawnParticle(particle.getParticlesType().getValue().get(), e.getLocation(), particle.getParticlesAmount().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesSpeed().getValue().get(), typeData);
                        } else {
                            e.getWorld().spawnParticle(particle.getParticlesType().getValue().get(), e.getLocation(), particle.getParticlesAmount().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesOffSet().getValue().get(), particle.getParticlesSpeed().getValue().get(), null);
                        }
                    }
                };
                runnable.runTaskTimerAsynchronously(SCore.plugin, 0L, particle.getParticlesDelay().getValue().get());*/

                Projectile projectile;
                if (e instanceof Projectile) projectile = (Projectile) e;
                else return;


                ParticleEffect particleEffect = ParticleEffect.valueOf(particle.getParticlesType().getValue().get().name());
                float speed = particle.getParticlesSpeed().getValue().get().floatValue();
                float offset =  particle.getParticlesOffSet().getValue().get().floatValue();

                ParticleBuilder builder = new ParticleBuilder(particleEffect).setOffset(offset, offset, offset).setSpeed(speed).setAmount(particle.getParticlesAmount().getValue().get().intValue());
                if (particle.canHaveRedstoneColor()) {
                    builder.setParticleData(new RegularColor(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue()));
                    if (particle.getRedstoneColor().getValue().isPresent()) {
                        Color color = particle.getRedstoneColor().getValue().get();
                        builder.setParticleData(new RegularColor(color.getRed(), color.getGreen(), color.getBlue()));
                    }
                } else if (particle.canHaveBlocktype()) {
                    builder.setParticleData(new BlockTexture(Material.STONE));
                    if (particle.getBlockType() != null)
                        builder.setParticleData(new BlockTexture(particle.getBlockType().getValue().get()));
                }


                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.isDead() || projectile.isOnGround()) cancel();

                        float particlesAmountForVector = 200;
                        float divide = 100;
                        /* between 1 and 200 */
                        float canlculDensity = 201 - particle.getParticlesDensity().getValue().get();

                        particlesAmountForVector = particlesAmountForVector / canlculDensity;
                        divide = divide / canlculDensity;

                        Vector vector = projectile.getVelocity();
                        for (float i = 1; i <= particlesAmountForVector; i++) {
                            float x = (float) i/divide;
                            Vector newVector = vector.clone().multiply(x);
                            builder.setLocation(projectile.getLocation().add(newVector)).display();
                        }
                    }
                };
                runnable.runTaskTimerAsynchronously(SCore.plugin, 0L, particle.getParticlesDelay().getValue().get());

            }
        }
    }
}
