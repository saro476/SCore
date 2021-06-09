package com.ssomar.score.commands.runnable.player.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommandTemplate;

public class FrontDash extends PlayerCommandTemplate{

	@Override
	public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		int amount = 5;
		
		try {
			amount = Integer.valueOf(args.get(0));
		}catch(NumberFormatException e){
			return;
		}
		
		Location pLoc = p.getLocation();
		pLoc.setPitch(0);
		Vector v = pLoc.getDirection();
		v.multiply(amount);
		p.setVelocity(v);
		
	}

	@Override
	public String verify(List<String> args) {
		String error ="";

		String frontdash= "FRONTDASH {number}";
		if(args.size()>1) error= tooManyArgs+frontdash;
		else if(args.size()<1) error = notEnoughArgs+frontdash;
		else if(args.size()==1) { 
			try {
				Integer.valueOf(args.get(0));
			}catch(NumberFormatException e){
				error = invalidTime+args.get(0)+" for command: "+frontdash;
			}
		}

		return error;
	}

}