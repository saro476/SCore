package com.ssomar.score.commands.runnable;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.ssomar.score.SCore;
import com.ssomar.score.utils.StringConverter;
import com.ssomar.score.utils.placeholders.StringPlaceholder;

public abstract class RunCommand {

	private String brutCommand;

	private StringPlaceholder sp;

	/* In Ticks */
	private int delay;

	private long runTime;

	private ActionInfo aInfo;
	
	private UUID uuid;
	
	private BukkitTask task;

	public RunCommand(String brutCommand, int delay, ActionInfo aInfo) {
		this.brutCommand = brutCommand;
		this.aInfo = aInfo;
		this.sp = aInfo.getSp();
		this.delay = delay;
		this.runTime = -1;
		this.uuid = UUID.randomUUID();
		this.task = null;
		this.pickupInfo();
	}

	public abstract void run();
	
	public void runCommand(CommandManager manager) {
		
		if(delay == 0) runTime = 0;
		else {
			runTime = System.currentTimeMillis() + delay * 50;
		}
		
		String finalCommand = this.getBrutCommand();
		
		if(getBrutCommand().contains("ei giveslot")) {
			try {
				String playeName = getBrutCommand().split("ei giveslot ")[1].split(" ")[0];
				Player pgive = Bukkit.getPlayer(playeName);
				CommandsHandler.getInstance().addStopPickup(pgive, 20);
			}catch(Exception e) {}
		}
		
		finalCommand = this.getSp().replacePlaceholder(finalCommand);
		SCommand command = manager.getCommand(finalCommand); 
		if(command != null) {
			List<String> args = manager.getArgs(finalCommand);
			this.runCommand(command, args);
		}
		else {
			if(finalCommand.charAt(0)=='/') finalCommand = finalCommand.substring(1, finalCommand.length());
			// accept the "color": HEX COLOR in title
			if(finalCommand.contains("\"color\"") && finalCommand.contains("title")) finalCommand = StringConverter.deconvertColor(finalCommand);
			RunConsoleCommand.runConsoleCommand(finalCommand, aInfo.isSilenceOutput());
		}
	}
	
	public void runDelayedCommand() {
		BukkitRunnable runnable = new BukkitRunnable(){

			public void run(){		
				insideDelayedCommand();
			}
		};
		task = runnable.runTaskLater(SCore.getPlugin(), this.getDelay());
		CommandsHandler.getInstance().addDelayedCommand(this);
	}
	
	public abstract void insideDelayedCommand();
	
	public abstract void runCommand(SCommand command , List<String> args);

	public abstract void pickupInfo();


	public String getBrutCommand() {
		return brutCommand;
	}

	public void setBrutCommand(String brutCommand) {
		this.brutCommand = brutCommand;
	}

	public StringPlaceholder getSp() {
		return sp;
	}

	public void setSp(StringPlaceholder sp) {
		this.sp = sp;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}

	public ActionInfo getaInfo() {
		return aInfo;
	}

	public void setaInfo(ActionInfo aInfo) {
		this.aInfo = aInfo;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public BukkitTask getTask() {
		return task;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}
	
}
