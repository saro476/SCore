package com.ssomar.score.conditions.condition.player.basics;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.player.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import org.bukkit.entity.Player;

import java.util.Optional;

public class IfNotFlying extends PlayerCondition<Boolean, String> {

    public IfNotFlying() {
        super(ConditionType.BOOLEAN, "ifNotFlying", "If not flying", new String[]{}, false, " &cYou must not fly to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender) {
        if (getAllCondition(messageSender.getSp()) && player.isFlying()) {
            sendErrorMsg(playerOpt, messageSender);
            return false;
        }
        return true;
    }
}