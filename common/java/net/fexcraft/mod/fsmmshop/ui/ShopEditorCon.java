package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fsmmshop.FSMMShop;
import net.fexcraft.mod.fsmmshop.Shop;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.ui.ContainerInterface;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEditorCon extends ContainerInterface {

	protected Shop shop;

	public ShopEditorCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
		shop = FSMMShop.getShopAt(ply.entity.getWorld().local(), pos);
		inventory = shop.inventory;
	}

}
