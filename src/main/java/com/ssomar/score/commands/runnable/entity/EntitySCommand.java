package com.ssomar.score.commands.runnable.entity;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;

public interface EntitySCommand {

	void run(Player p, Entity entity, List<String> args, ActionInfo aInfo);

	String verify(List<String> args);

}
