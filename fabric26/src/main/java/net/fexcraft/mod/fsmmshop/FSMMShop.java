package net.fexcraft.mod.fsmmshop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.fcl.util.PacketTag21;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.packet.PacketTag;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.io.File;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FSMMShop implements ModInitializer {

	public static final String MODID = "fsmmshop";
	public static FSConfig CONFIG;
	//
	public static BlockEntityType<ShopEntity> SHOP_ENT;

	@Override
	public void onInitialize(){
		CONFIG = new FSConfig(new File(FabricLoader.getInstance().getConfigDir().toAbsolutePath().toFile(), "fsmmshop.json"));
		FCL.registerBlock("fsmmshop:shop", prop -> new ShopBlock(prop));
		SHOP_ENT = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "fsmmshop:shop", FabricBlockEntityTypeBuilder.create(ShopEntity::new, ShopBlock.INST).build());
		UniReg.registerMod(MODID, this);
		FSUI.register();
		UniFCL.regTagPacketListener(MODID, true, (com, player) -> {
			getShopAt(player.getWorld().local(), com.getV3I("pos")).read(com);
		});
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
			content.accept(new ItemStack(ShopBlock.INST), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
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
		//TODO ranged
		for(ServerPlayer player : FCL.SERVER.get().getPlayerList().getPlayers()){
			ServerPlayNetworking.getSender(player).sendPacket(new PacketTag21(MODID, com));
		}
	}

}
