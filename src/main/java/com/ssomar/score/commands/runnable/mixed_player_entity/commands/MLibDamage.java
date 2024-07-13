package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.usedapi.MyhticLibAPI;
import com.ssomar.score.usedapi.WorldGuardAPI;
import com.ssomar.score.utils.numbers.NTools;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MLibDamage extends MixedCommand {

    public void run(Player p, Entity receiver, List<String> args, ActionInfo aInfo) {

        if(!(receiver instanceof LivingEntity)) return;
        LivingEntity livingReceiver = (LivingEntity) receiver;

        /* When target a NPC it can occurs */
        if (receiver == null) return;

        double damage = getDamage(p, livingReceiver, args, aInfo);
        // MAGIC, PHYSICAL, WEAPON, SKILL, PROJECTILE, UNARMED, ON_HIT, MINION, DOT;
        String damageType = args.get(1).toUpperCase();
        String element = null;
        if (args.size() >= 4) element = args.get(3);

        if (damage > 0 && !receiver.isDead()) {
            int maximumNoDmg = livingReceiver.getNoDamageTicks();
            livingReceiver.setNoDamageTicks(0);
            boolean doDamage = true;
            if (SCore.hasWorldGuard && receiver instanceof Player) doDamage = WorldGuardAPI.isInPvpZone((Player) receiver, receiver.getLocation());
            if (doDamage) {
                if (p != null) {
                    /* To avoid looping damage */
                    if(aInfo.isActionRelatedToDamageEvent()) p.setMetadata("cancelDamageEvent", (MetadataValue) new FixedMetadataValue((Plugin) SCore.plugin, Integer.valueOf(7772)));
                    p.setMetadata("damageFromCustomCommand", (MetadataValue) new FixedMetadataValue((Plugin) SCore.plugin, Integer.valueOf(7773)));
                    MyhticLibAPI.mlib_damage(p, livingReceiver, damage, damageType, element, Boolean.parseBoolean(args.get(2)));
                } else {
                    MyhticLibAPI.mlib_damage(null, livingReceiver, damage, damageType, element, Boolean.parseBoolean(args.get(2)));
                }
            }

            livingReceiver.setNoDamageTicks(maximumNoDmg);
        }
    }

    @SuppressWarnings("deprecation")
    public static double getDamage(Player launcher, LivingEntity receiver, List<String> args, ActionInfo actionInfo) {
        double amount;
        String damage = args.get(0);

        boolean potionAmplification = false;
        if (args.size() >= 2) {
            potionAmplification = Boolean.valueOf(args.get(1));
        }
        boolean attributeAmplification = false;
        if (args.size() >= 3) {
            attributeAmplification = Boolean.valueOf(args.get(2));
        }

        /* percentage damage */
        if (damage.contains("%")) {
            String[] decomp = damage.split("%");
            damage = decomp[0];
            damage = damage.trim();
            if (damage.length() == 1) {
                damage = "0" + damage;
            }

            double percentage = damage.equals("100") ? 1 : Double.parseDouble("0." + damage);
            amount = receiver.getMaxHealth() * percentage;
            amount = NTools.reduceDouble(amount, 2);
        } else amount = Double.parseDouble(damage);

        if (launcher != null) {
            if (potionAmplification) {
                PotionEffectType incDamage = SCore.is1v20v5Plus() ? PotionEffectType.STRENGTH : PotionEffectType.getByName("INCREASE_DAMAGE");
                PotionEffect pE = launcher.getPotionEffect(incDamage);
                if (pE != null) {
                    amount = amount + (pE.getAmplifier() + 1) * 3;
                }
            }

            //SsomarDev.testMsg("boost attribute: "+ attributeAmplification);
            if (attributeAmplification) {
                AttributeInstance aI = launcher.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                double bonusAmount = 0;
                if (aI != null) {
                    //SsomarDev.testMsg("damage value: "+aI.getValue());
                    for (AttributeModifier aM : aI.getModifiers()) {
                        //SsomarDev.testMsg("passe 2:  "+aM.getOperation());
                        if (aM.getOperation().equals(AttributeModifier.Operation.MULTIPLY_SCALAR_1)) {
                            //SsomarDev.testMsg("passe 3: "+(amount * aM.getAmount())+ " >> "+aM.getAmount());
                            bonusAmount = bonusAmount + amount * aM.getAmount();
                        }
                    }
                }
                //SsomarDev.testMsg("boost attribute bonus: "+ bonusAmount);
                amount = amount + bonusAmount;
            }
        }
        return amount;
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        return staticVerif(args, isFinalVerification, getTemplate());
    }

    public static Optional<String> staticVerif(List<String> args, boolean isFinalVerification, String template) {
        if (args.size() < 3) return Optional.of(notEnoughArgs + template);

        ArgumentChecker ac = checkDouble(args.get(0), isFinalVerification, template, true);
        if (!ac.isValid()) return Optional.of(ac.getError());

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("MLIB_DAMAGE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "MLIB_DAMAGE {number} {damageType} {knockback} [element]";
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