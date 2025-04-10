package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fsmmshop.FSMMShop;
import net.fexcraft.mod.fsmmshop.Shop;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.fexcraft.mod.uni.world.WrapperHolder;

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

	@Override
	public void packet(TagCW com, boolean client){
		//FCL.LOGGER.debug(com);
		String task = com.getString("task");
		switch(task){
			case "admin":{
				if(WrapperHolder.isSinglePlayer() || WrapperHolder.isOp(player.entity)){
					shop.admin = !shop.admin;
					if(!client) sendUpdate();
				}
				return;
			}
			case "item":{
				if(shop.owner == null) shop.setOwner(player);
				shop.stack = root.getPickedStack().copy();
				shop.stack.count(1);
				if(!client) sendUpdate();
				return;
			}
			case "price":{
				if(shop.owner == null) shop.setOwner(player);
				shop.price = com.getLong("price");
				if(!client) sendUpdate();
				return;
			}
			case "sell":{
				if(shop.owner == null) shop.setOwner(player);
				shop.sell = true;
				if(!client) sendUpdate();
				return;
			}
			case "buy":{
				if(shop.owner == null) shop.setOwner(player);
				shop.sell = false;
				if(!client) sendUpdate();
				return;
			}
		}
	}

	private void sendUpdate(){
		FSMMShop.updateShop(player.entity.getWorld().local(), pos);
	}

}
