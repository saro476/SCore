package com.ssomar.score.commands.runnable.entity.commands;

import java.util.List;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.entity.EntityCommandTemplate;

/* STRIKELIGHTNING */
public class StrikeLightning extends EntityCommandTemplate{

	@Override
	public void run(Player p, Entity entity, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		if(!entity.isDead()) {
			entity.getWorld().strikeLightningEffect(entity.getLocation());
			if(entity instanceof Creeper) {
				((Creeper)entity).setPowered(true);
			}
		}
	}

	@Override
	public String verify(List<String> args) {
		return "";
	}

}