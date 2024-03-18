package com.ssomar.score.features.custom.conditions.entity.condition;

import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionRequest;
import com.ssomar.score.features.types.BooleanFeature;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;

public class IfPowered extends EntityConditionFeature<BooleanFeature, IfPowered> {

    public IfPowered(FeatureParentInterface parent) {
        super(parent, "ifPowered", "If powered", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public boolean verifCondition(EntityConditionRequest request) {
        Entity entity = request.getEntity();
        if (getCondition().getValue(request.getSp()) && entity instanceof Creeper && !((Creeper) entity).isPowered()) {
            runInvalidCondition(request);
            return false;
        }

        return true;
    }

    @Override
    public IfPowered getValue() {
        return this;
    }

    @Override
    public void subReset() {
        setCondition(new BooleanFeature(getParent(), "ifPowered", false, "If powered", new String[]{}, Material.ANVIL, false, true));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().isConfigured();
    }

    @Override
    public IfPowered getNewInstance(FeatureParentInterface parent) {
        return new IfPowered(parent);
    }
}
