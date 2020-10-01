package kr.o_r.prodzpod.yacore.init;

import kr.o_r.prodzpod.yacore.YaCore;
import kr.o_r.prodzpod.yacore.YaCoreConfig;
import kr.o_r.prodzpod.yacore.world.NewEndCityGen;
import kr.o_r.prodzpod.yacore.world.NewNetherFortressGen;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

import static net.minecraft.world.gen.structure.MapGenStructureIO.registerStructure;

public class WorldHandler {
    @SubscribeEvent
    public void onInitMapGen(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.END_CITY) {
            try {
                registerStructure(NewEndCityGen.Start.class, "EndCity");
                MapGenEndCity clazz = (MapGenEndCity)event.getOriginalGen();
                Field provider = clazz.getClass().getDeclaredField("field_186133_d");
                provider.setAccessible(true);
                event.setNewGen(new NewEndCityGen((ChunkGeneratorEnd) provider.get(clazz)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (event.getType() == InitMapGenEvent.EventType.NETHER_BRIDGE) {
            registerStructure(NewNetherFortressGen.Start.class, "Fortress");
            event.setNewGen(new NewNetherFortressGen());
        }
    }
}
