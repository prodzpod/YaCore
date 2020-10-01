package kr.o_r.prodzpod.yacore.world;

import kr.o_r.prodzpod.yacore.YaCoreConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import java.util.List;
import java.util.Random;

public class NewNetherFortressGen extends MapGenNetherBridge {
    private static final int HASH = 39071882;
    public static int minSpacing = 8;

    public NewNetherFortressGen() {
        super();
        minSpacing = Math.max(minSpacing, 16 - YaCoreConfig.fortress.fortressSpacing);
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        Random random = this.world.setRandomSeed(
                (chunkX < 0 ? chunkX - (YaCoreConfig.fortress.fortressSpacing - 1) : chunkX) / YaCoreConfig.fortress.fortressSpacing,
                (chunkZ < 0 ? chunkZ - (YaCoreConfig.fortress.fortressSpacing - 1) : chunkZ) / YaCoreConfig.fortress.fortressSpacing, HASH);
        return (chunkX % 16 < minSpacing && chunkZ % 16 < minSpacing && random.nextInt(YaCoreConfig.fortress.fortressSpawnRate) == 0);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new NewNetherFortressGen.Start(this.world, this.rand, chunkX, chunkZ);
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, YaCoreConfig.fortress.fortressSpacing, minSpacing, HASH, true, 1000, findUnexplored);
    }

    public static class Start extends StructureStart
    {
        public Start() {}
        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurenetherbridgepieces$start);
            structurenetherbridgepieces$start.buildComponent(structurenetherbridgepieces$start, this.components, random);
            List<StructureComponent> list = structurenetherbridgepieces$start.pendingChildren;

            while (!list.isEmpty())
            {
                int i = random.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurenetherbridgepieces$start, this.components, random);
            }

            this.updateBoundingBox();
            this.setRandomHeight(worldIn, random, YaCoreConfig.fortress.minHeight, YaCoreConfig.fortress.maxHeight);
        }
    }
}
