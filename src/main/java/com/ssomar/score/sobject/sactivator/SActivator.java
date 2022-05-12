package com.ssomar.score.sobject.sactivator;

import java.util.List;

import com.plotsquared.core.configuration.ConfigurationSection;
import com.ssomar.score.sobject.HigherFormSObject;
import com.ssomar.sevents.events.projectile.hitentity.ProjectileHitEntityEvent;
import com.ssomar.score.conditions.condition.blockcondition.BlockConditions;
import com.ssomar.score.conditions.condition.customei.CustomEIConditions;
import com.ssomar.score.conditions.condition.entity.EntityConditions;
import com.ssomar.score.conditions.condition.item.ItemConditions;
import com.ssomar.score.conditions.condition.player.PlayerConditions;
import com.ssomar.score.conditions.condition.world.WorldConditions;
import com.ssomar.score.conditions.condition.placeholders.PlaceholdersCondition;
import com.ssomar.score.sobject.sactivator.requiredei.RequiredEI;
import com.ssomar.sevents.events.projectile.hitplayer.ProjectileHitPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public interface SActivator {
	
	String getParentObjectID();
	
	String getID();

	SOption getOption();
	
	/* Delay */
	boolean isDelayInTick();
	
	int getDelay();
	
	/* Conditions */
	PlayerConditions getOwnerConditions();
	
	PlayerConditions getPlayerConditions();
	
	PlayerConditions getTargetPlayerConditions();
	
	WorldConditions getWorldConditions();
	
	ItemConditions getItemConditions();
	
	BlockConditions getBlockConditions();
	
	EntityConditions getTargetEntityConditions();
	
	BlockConditions getTargetBlockConditions();
	
	CustomEIConditions getCustomEIConditions();
	
	List<PlaceholdersCondition> getPlaceholdersConditions();
	
	/* Required ExecutableItems */
	List<RequiredEI> getRequiredExecutableItems();
	
	RequiredEI getRequiredEI(String id);
	
	/* Detailed blocks */
	DetailedBlocks getDetailedBlocks();

	void run(HigherFormSObject parentObject, EventInfo eventInfo);

	List<String> getMenuDescription();

	void startEditing(Player editor);

	void save(ConfigurationSection config);

	void delete();

	static void cancelEvent(Event e, boolean condition) {
		if (e != null && condition && e instanceof Cancellable) {
			/* IMPORTANT? IF THE PROJECTILE IS NOT REMOVED THE SERVER CRASH ! */
			if(e instanceof ProjectileHitEntityEvent){
				((ProjectileHitEntityEvent)e).getEntity().remove();
			}
			else if(e instanceof ProjectileHitPlayerEvent){
				((ProjectileHitPlayerEvent)e).getEntity().remove();
			}
			((Cancellable) e).setCancelled(true);
		}
	}
}
