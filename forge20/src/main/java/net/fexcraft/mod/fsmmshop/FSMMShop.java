package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.util.TagPacket;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
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
	public static final DeferredRegister<BlockEntityType<?>> BLKENT = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
	//
	public static final RegistryObject<Block> SHOP_BLK = BLOCKS.register("shop", () -> new ShopBlock());
	public static final RegistryObject<Item> SHOP_ITEM = ITEMS.register("shop", () -> new BlockItem(SHOP_BLK.get(), new Item.Properties()));
	public static final RegistryObject<BlockEntityType<ShopEntity>> SHOP_ENT = BLKENT.register("shop", () ->
		BlockEntityType.Builder.of(ShopEntity::new, SHOP_BLK.get()).build(null));

	public FSMMShop(){
		CONFIG = new FSConfig(new File(FMLPaths.CONFIGDIR.get().toFile(), "fsmmshop.json"));
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLKENT.register(FMLJavaModLoadingContext.get().getModEventBus());
		UniReg.registerMod(MODID, this);
		FSUI.register();
		FCL.addListener(MODID, true, (com, player) -> {
			getShopAt(player.getWorld().local(), com.getV3I("pos")).read(com);
		});
	}

	public static Shop getShopAt(Level level, V3I pos){
		return ((ShopEntity)level.getBlockEntity(new BlockPos(pos.x, pos.y, pos.z))).shop;
	}

	public static void updateShop(Level level, V3I vec){
		BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
		Shop shop = ((ShopEntity)level.getBlockEntity(pos)).shop;
		TagCW com = TagCW.create();
		com.set("pos", vec);
		shop.write(com);
		LevelChunk chunk = level.getChunkAt(pos);
		FCL.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new TagPacket(MODID, com));
	}

}
