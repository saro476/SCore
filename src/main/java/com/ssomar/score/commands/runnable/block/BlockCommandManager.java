package com.ssomar.score.commands.runnable.block;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.CommandManager;
import com.ssomar.score.commands.runnable.block.commands.*;
import com.ssomar.score.commands.runnable.block.commands.settempblock.SetTempBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockCommandManager extends CommandManager<BlockCommand> {

    private static BlockCommandManager instance;

    public BlockCommandManager() {
        List<BlockCommand> references = new ArrayList<>();
        references.add(new ApplyBoneMeal());
        references.add(new SetBlockPos());
        references.add(new SetBlock());
        references.add(new SetTempBlock());
        references.add(new SetExecutableBlock());
        references.add(new SendMessage());
        references.add(new Explode());
        references.add(new Break());
        references.add(new Launch());
        references.add(new ChangeBlockType());
        references.add(new DropItem());
        references.add(new DropExecutableItem());
        references.add(new DropExecutableBlock());
        references.add(new MineInCube());
        references.add(new RemoveBlock());
        references.add(new Around());
        references.add(new MobAround());
        references.add(new VeinBreaker());
        references.add(new SilkSpawner());
        references.add(new DrainInCube());
        references.add(new Move());
        references.add(new StrikeLightning());
        /* No BlockData in 1.12 and less */
        if (!SCore.is1v12Less()) {
            references.add(new FarmInCube());
            references.add(new FertilizeInCube());
        }
        if (!SCore.is1v11Less()) {
            references.add(new ParticleCommand());
        }
        references.add(new SellContent());
        setCommands(references);
    }

    public static BlockCommandManager getInstance() {
        if (instance == null) instance = new BlockCommandManager();
        return instance;
    }
}
