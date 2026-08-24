package net.fexcraft.mod.fsmmshop;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import static net.fexcraft.mod.fsmmshop.FSMMShop.SHOP_ENT;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FSMMShopC implements ClientModInitializer {

	@Override
	public void onInitializeClient(){
		BlockEntityRenderers.register(SHOP_ENT, ShopRenderer::new);
	}

}
