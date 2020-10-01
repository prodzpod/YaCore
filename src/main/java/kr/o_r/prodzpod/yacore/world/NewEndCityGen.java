package kr.o_r.prodzpod.yacore.world;

import kr.o_r.prodzpod.yacore.YaCore;
import kr.o_r.prodzpod.yacore.YaCoreConfig;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;
// i really dont like copypasting code with minor touchup... except when the base code MAKES A FIELD AND DOESNT USE IT, HARDCODES VALUE ANYWAYS
public class NewEndCityGen extends MapGenEndCity {
    private final ChunkGeneratorEnd endProvider;
    private static final int HASH = 10387313;

    public NewEndCityGen(ChunkGeneratorEnd endProvider) {
        super(endProvider);
        this.endProvider = endProvider;
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        Random random = this.world.setRandomSeed(
                (chunkX < 0 ? chunkX - (YaCoreConfig.endCity.citySpacing - 1) : chunkX) / YaCoreConfig.endCity.citySpacing,
                (chunkZ < 0 ? chunkZ - (YaCoreConfig.endCity.citySpacing - 1) : chunkZ) / YaCoreConfig.endCity.citySpacing, HASH);
        if (random.nextInt(YaCoreConfig.endCity.endCitySpawnRate) == 0 && this.endProvider.isIslandChunk(chunkX, chunkZ))
            return getYPosForStructure(chunkX, chunkZ, this.endProvider) >= YaCoreConfig.endCity.minHeight;
        else return false;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new NewEndCityGen.Start(this.world, this.endProvider, this.rand, chunkX, chunkZ);
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, YaCoreConfig.endCity.citySpacing, YaCoreConfig.endCity.minCitySeparation, HASH, true, 100, findUnexplored);
    }

    private static int getYPosForStructure(int chunkX, int chunkZ, ChunkGeneratorEnd endProvider)
    {
        Random random = new Random(chunkX + chunkZ * 10387313);
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        ChunkPrimer chunkprimer = new ChunkPrimer();
        endProvider.setBlocksInChunk(chunkX, chunkZ, chunkprimer);
        int xOffset, yOffset;
        switch (rotation) {
            case CLOCKWISE_90:
                xOffset = -5;
                yOffset = 5;
                break;
            case CLOCKWISE_180:
                xOffset = -5;
                yOffset = -5;
                break;
            case COUNTERCLOCKWISE_90:
                xOffset = 5;
                yOffset = -5;
                break;
            default:
                xOffset = 5;
                yOffset = 5;
                break;
        }

        int k = chunkprimer.findGroundBlockIdx(7, 7);
        k = Math.min(k, chunkprimer.findGroundBlockIdx(7, 7 + yOffset));
        k = Math.min(k, chunkprimer.findGroundBlockIdx(7 + xOffset, 7));
        k = Math.min(k, chunkprimer.findGroundBlockIdx(7 + xOffset, 7 + yOffset));
        return k;
    }

    public static class Start extends StructureStart
    {
        private boolean isSizeable;

        public Start() {}
        public Start(World worldIn, ChunkGeneratorEnd chunkProvider, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            this.create(worldIn, chunkProvider, random, chunkX, chunkZ);
        }

        private void create(World worldIn, ChunkGeneratorEnd chunkProvider, Random rnd, int chunkX, int chunkZ)
        {
            Random random = new Random(chunkX + chunkZ * NewEndCityGen.HASH);
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            int i = NewEndCityGen.getYPosForStructure(chunkX, chunkZ, chunkProvider);

            if (i < YaCoreConfig.endCity.minHeight)
            {
                this.isSizeable = false;
            }
            else
            {
                BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                StructureEndCityPieces.startHouseTower(worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
                this.updateBoundingBox();
                this.isSizeable = true;
            }
        }

        public boolean isSizeableStructure()
        {
            return this.isSizeable;
        }
    }
}
