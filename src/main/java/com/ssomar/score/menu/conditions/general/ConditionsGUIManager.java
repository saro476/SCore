package com.ssomar.score.menu.conditions.general;

import com.ssomar.score.SsomarDev;
import com.ssomar.score.conditions.Conditions;
import com.ssomar.score.conditions.condition.Condition;
import com.ssomar.score.conditions.condition.conditiontype.AConditionTypeWithSubMenu;
import com.ssomar.score.conditions.managers.ConditionsManager;
import com.ssomar.score.menu.conditions.home.ConditionsHomeGUIManager;
import com.ssomar.score.menu.score.GUIManagerSCore;
import com.ssomar.score.menu.score.InteractionClickedGUIManager;
import com.ssomar.score.sobject.SObject;
import com.ssomar.score.sobject.sactivator.SActivator;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import org.bukkit.entity.Player;

import java.util.List;

public class ConditionsGUIManager extends GUIManagerSCore<ConditionsGUI> {

    private static ConditionsGUIManager instance;

    public void startEditing(Player p, SPlugin sPlugin, SObject sObject, SActivator sActivator, String detail, Conditions conditions, ConditionsManager conditionsManager) {
        cache.put(p, new ConditionsGUI(sPlugin, sObject, sActivator, detail, conditions, conditionsManager));
        cache.get(p).openGUISync(p);
    }

    @Override
    public boolean allClicked(InteractionClickedGUIManager i) {

        String itemName = StringConverter.decoloredString(i.name);

        if(itemName.contains("Back")) {
            ConditionsHomeGUIManager.getInstance().startEditing(i.player, i.sPlugin, i.sObject, i.sActivator);
        }
        else if(!itemName.contains("ERROR ID")) {
            ConditionsGUI gui = cache.get(i.player);

            Condition condition;
            if(cache.get(i.player).getConditions().contains(itemName)) {
                condition = gui.getConditions().get(itemName);
                SsomarDev.testMsg("Condition found: " + condition.getConfigName());
            }
            else{
                condition = (Condition) gui.getConditionsManager().get(itemName).clone();
                SsomarDev.testMsg("Condition not found: " + condition.getConfigName());

                /* Problem of link, I need to clear it otherwise it takes the list of the previous edit */
                if(condition.getCondition() instanceof List) {
                    ((List<?>) condition.getCondition()).clear();
                }
            }

            if(condition.getConditionType().getInstance() instanceof AConditionTypeWithSubMenu)
                ((AConditionTypeWithSubMenu)condition.getConditionType().getInstance()).openSubMenu(this, condition, i.player);
            else
                ConditionGUIManager.getInstance().startEditing(i.player,i.sPlugin, i.sObject, i.sActivator, gui.getDetail(), gui.getConditions(), gui.getConditionsManager(), condition);
        }
        return true;
    }

    @Override
    public boolean noShiftLeftclicked(InteractionClickedGUIManager interact) {
        return false;
    }

    @Override
    public boolean noShiftRightclicked(InteractionClickedGUIManager interact) {
        return false;
    }

    @Override
    public boolean shiftClicked(InteractionClickedGUIManager i) {
        return false;
    }

    @Override
    public boolean shiftLeftClicked(InteractionClickedGUIManager interact) {
        return false;
    }

    @Override
    public boolean shiftRightClicked(InteractionClickedGUIManager interact) {
        return false;
    }

    @Override
    public boolean leftClicked(InteractionClickedGUIManager interact) {
        return false;
    }

    @Override
    public boolean rightClicked(InteractionClickedGUIManager interact) {
        return false;
    }

    public static ConditionsGUIManager getInstance() {
        if(instance == null) instance = new ConditionsGUIManager();
        return instance;
    }

    @Override
    public void saveTheConfiguration(Player p) {

    }

    @Override
    public boolean noShiftclicked(InteractionClickedGUIManager<ConditionsGUI> i) {
        return false;
    }
}