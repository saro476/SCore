package com.ssomar.score.events;

import com.ssomar.executableblocks.ExecutableBlocks;
import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.score.SCore;
import com.ssomar.score.usedapi.AllWorldManager;
import com.ssomar.score.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.events.BentoBoxReadyEvent;

public class BentoBoxLoadingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void bentoBoxReady(BentoBoxReadyEvent e) {

        if(SCore.hasExecutableItems) ExecutableItems.plugin.onReload(false);
        if(SCore.hasExecutableBlocks) ExecutableBlocks.plugin.onReload(false);
    }
}