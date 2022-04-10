package com.ssomar.score.sobject.sactivator.conditions.condition.blockcondition.basics;

import com.ssomar.score.sobject.sactivator.conditions.condition.ConditionType;
import com.ssomar.score.sobject.sactivator.conditions.condition.blockcondition.BlockCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.score.utils.StringCalculation;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;

public class IfBlockLocationZ extends BlockCondition<String> {

    public IfBlockLocationZ() {
        super(ConditionType.NUMBER_CONDITION, "ifBlockLocationZ", "If block location Z", new String[]{}, "", " &cThe block location Z is invalid to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Block b, Optional<Player> playerOpt, SendMessage messangeSender) {
        if(!getCondition().equals("") && !StringCalculation.calculation(getCondition(), b.getLocation().getZ())) {
            sendErrorMsg(playerOpt, messangeSender);
            return false;
        }
        return true;
    }
}
