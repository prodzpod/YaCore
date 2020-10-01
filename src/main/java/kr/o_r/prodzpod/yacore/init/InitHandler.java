package kr.o_r.prodzpod.yacore.init;

import kr.o_r.prodzpod.yacore.YaCoreConfig;
import kr.o_r.prodzpod.yacore.recipe.CustomTAImpetusRecipe;
import kr.o_r.prodzpod.yacore.util.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InitHandler {
    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (Loader.isModLoaded("thaumicaugmentation") && YaCoreConfig.compat.impetusAmount != 0) {
            System.out.println("TA SPECIFICATION DETECTED");
            event.getRegistry().register(new CustomTAImpetusRecipe().setRegistryName(new ResourceLocation(Reference.MODID, "impetus_jewel_new")));
        }
    }

    @SubscribeEvent
    public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        }
    }
}