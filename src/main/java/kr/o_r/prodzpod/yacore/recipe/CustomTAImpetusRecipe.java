package kr.o_r.prodzpod.yacore.recipe;

import kr.o_r.prodzpod.yacore.YaCore;
import kr.o_r.prodzpod.yacore.YaCoreConfig;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thaumcraft.api.items.ItemsTC;
import thecodex6824.thaumicaugmentation.ThaumicAugmentation;
import thecodex6824.thaumicaugmentation.api.TAItems;
import thecodex6824.thaumicaugmentation.api.augment.Augment;
import thecodex6824.thaumicaugmentation.api.augment.CapabilityAugment;
import thecodex6824.thaumicaugmentation.api.impetus.CapabilityImpetusStorage;
import thecodex6824.thaumicaugmentation.api.impetus.IImpetusStorage;
import thecodex6824.thaumicaugmentation.api.impetus.ImpetusStorage;

import java.util.Arrays;

public class CustomTAImpetusRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    // i am desperate

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return validateRecipe(inv);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (validateRecipe(inv)) return new ItemStack(TAItems.MATERIAL, YaCoreConfig.compat.impetusResult, 5);
        else return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        CraftingSlot slot = getConductor(inv);
        if (slot != null) {
            ItemStack stack = slot.stack.copy();
            IImpetusStorage energy = stack.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, null);
            for (int i = 0; i < (YaCoreConfig.compat.impetusAmount / 10); i++) {
                energy.extractEnergy(10, false); // tfw max transfer rate is 15 so you 10x10 your 100 away
            }
            energy.extractEnergy(YaCoreConfig.compat.impetusAmount % 10, false);
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !ThaumicAugmentation.proxy.isSingleplayer()) {
                if (!stack.hasTagCompound())
                    stack.setTagCompound(new NBTTagCompound());

                stack.getTagCompound().setTag("cap", new NBTTagCompound());
                stack.getTagCompound().getCompoundTag("cap").setTag("augment", ((Augment) stack.getCapability(CapabilityAugment.AUGMENT, null)).serializeNBT());
                stack.getTagCompound().getCompoundTag("cap").setTag("energy", ((ImpetusStorage) energy).serializeNBT());
            }

            ret.set(slot.slot, stack);
        }
        return ret;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    private boolean validateRecipe(InventoryCrafting inv) {
        boolean diamond = false;
        ItemStack conductor = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) { // pass if empty
                String[] ids = YaCoreConfig.compat.impetusItem.split(":");
                if (stack.getItem() == Item.getByNameOrId(ids[0] + ":" + ids[1]) && stack.getMetadata() == Integer.parseInt(ids[2]) && (!diamond)) diamond = true;
                else if ((stack.getItem() == TAItems.AUGMENT_CASTER_RIFT_ENERGY_STORAGE) && (conductor.isEmpty())) conductor = stack;
                else return false;
            }
        }
        if (diamond && !conductor.isEmpty()) {
            IImpetusStorage storage = conductor.getCapability(CapabilityImpetusStorage.IMPETUS_STORAGE, null);
            if (storage == null) return false;
            return storage.getEnergyStored() >= 100;
        } else return false;
    }

    private CraftingSlot getConductor(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack temp = inv.getStackInSlot(i);
            if (temp.getItem() == TAItems.AUGMENT_CASTER_RIFT_ENERGY_STORAGE) return new CraftingSlot(temp, i);
        }
        return null;
    }

    private static class CraftingSlot {
        public ItemStack stack;
        public int slot;
        public CraftingSlot(ItemStack stack, int slot) {
            this.stack = stack;
            this.slot = slot;
        }
    }
}
