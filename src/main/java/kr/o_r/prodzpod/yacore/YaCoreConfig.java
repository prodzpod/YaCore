package kr.o_r.prodzpod.yacore;

import kr.o_r.prodzpod.yacore.util.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID)
public class YaCoreConfig {
    @Config.Comment("Compatibility Settings")
    public static final Compat compat = new Compat();
    @Config.Comment("End City Generation Customization")
    public static final EndCity endCity = new EndCity();
    @Config.Comment("Nether Fortress Generation Customization")
    public static final Fortress fortress = new Fortress();

    public static class Compat {
        @Config.Comment("Amount of Impetus needed to craft Impetus Jewel(s). set to 0 to disable crafting.")
        @Config.RangeInt(min = 0, max = 300)
        @Config.Name("Impetus Amount")
        public int impetusAmount = 100;

        @Config.Comment("Item used to craft Impetus Jewel(s). modid:item:meta")
        @Config.Name("Impetus Item")
        public String impetusItem = "thaumcraft:void_seed:0";

        @Config.Comment("Amount of Impetus Jewel(s) per craft.")
        @Config.RangeInt(min = 1, max = 64)
        @Config.Name("Craft Result Amount")
        public int impetusResult = 2;
    }

    public static class EndCity {
        @Config.Comment("1/n chance to spawn this structure in each citySpacing^2 chunks.")
        @Config.RangeInt(min = 1)
        @Config.Name("End City Spawn Rate")
        public int endCitySpawnRate = 81;

        @Config.Comment("How many chunks should each end cities distance from each other? (in one direction)")
        @Config.RangeInt(min = 1)
        @Config.Name("End City Spacing")
        public int citySpacing = 20;

        @Config.Comment("minimum End City Seperation??")
        @Config.RangeInt(min = 1)
        @Config.Name("Minimum End City Seperation")
        public int minCitySeparation = 11;

        @Config.Comment("The lowest y coordinate Nether Fortresses will attempt to spawn.")
        @Config.RangeInt(min = 1, max = 255)
        @Config.Name("Minimum End City Base Height")
        public int minHeight = 60;
    }

    public static class Fortress {
        @Config.Comment("1/n chance to spawn this structure in each citySpacing^2 chunks.")
        @Config.RangeInt(min = 1)
        @Config.Name("Nether Fortresses Spawn Rate")
        public int fortressSpawnRate = 192;

        @Config.Comment("How many chunks should each nether fortresses distance from each other? (in one direction)")
        @Config.RangeInt(min = 1)
        @Config.Name("Nether Fortresses Spacing")
        public int fortressSpacing = 16;

        @Config.Comment("The lowest y coordinate Nether Fortresses will attempt to spawn.")
        @Config.RangeInt(min = 1, max = 255)
        @Config.Name("Minimum Nether Fortresses Base Height Minimum")
        public int minHeight = 48;

        @Config.Comment("The highest y coordinate Nether Fortresses will attempt to spawn.")
        @Config.RangeInt(min = 1, max = 255)
        @Config.Name("Minimum Nether Fortresses Base Height Maximum")
        public int maxHeight = 70;
    }
}
