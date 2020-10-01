package kr.o_r.prodzpod.yacore;

import kr.o_r.prodzpod.yacore.init.InitHandler;
import kr.o_r.prodzpod.yacore.init.WorldHandler;
import kr.o_r.prodzpod.yacore.util.Reference;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class YaCore {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);
    public static YaCore instance;

    public YaCore() {
        instance = this;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("hey, do this");
        MinecraftForge.EVENT_BUS.register(new InitHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new WorldHandler());
        String[] ids = YaCoreConfig.compat.impetusItem.split(":");
        LOGGER.info(Arrays.toString(ids));
    }
}
