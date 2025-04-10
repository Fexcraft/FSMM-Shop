package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fsmmshop.local.ShopBlock;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(FSMMShop.MODID)
public class FSMMShop {

	public static final String MODID = "fsmmshop";
	public static FSConfig CONFIG;
	//
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	//
	public static final RegistryObject<Block> ATM_BLOCK = BLOCKS.register("shop", () -> new ShopBlock());
	public static final RegistryObject<Item> ATM_ITEM = ITEMS.register("shop", () -> new BlockItem(ATM_BLOCK.get(), new Item.Properties()));

	public FSMMShop(){
		CONFIG = new FSConfig(new File(FMLPaths.CONFIGDIR.get().toFile(), "fsmmshop.json"));
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		FSUI.register();
	}

	public static Shop getShopAt(Object local, V3I pos){
		return null;
	}

	public static void updateShop(Object local, V3I pos){
		//
	}

}
