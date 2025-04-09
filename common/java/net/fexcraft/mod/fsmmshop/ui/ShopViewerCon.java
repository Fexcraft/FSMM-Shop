package net.fexcraft.mod.fsmmshop.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fsmm.data.Account;
import net.fexcraft.mod.fsmm.data.Bank;
import net.fexcraft.mod.fsmm.data.Manageable;
import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.fsmmshop.FSConfig;
import net.fexcraft.mod.fsmmshop.FSMMShop;
import net.fexcraft.mod.fsmmshop.Shop;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.ContainerInterface;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopViewerCon extends ContainerInterface {

	protected Account shopacc;
	protected Account plyacc;
	protected String msg;
	protected Shop shop;
	protected long bal;

	public ShopViewerCon(JsonMap map, UniEntity ply, V3I pos){
		super(map, ply, pos);
		shop = FSMMShop.getShopAt(ply.entity.getWorld().local(), pos);
		shopacc = shop.account();
		plyacc = ply.getApp(PlayerAccData.class).getAccount();
	}

	@Override
	public void packet(TagCW com, boolean client){
		//FCL.LOGGER.debug(com);
		String task = com.getString("task");
		switch(task){
			case "act":{
				if(shop.price <= 0){
					msg = "ui.fsmmshop.shop_viewer.inactive";
					sendSync();
					return;
				}
				if(shopacc == null && !shop.admin){
					msg = "ui.fsmmshop.shop_viewer.no_account";
					sendSync();
					return;
				}
				if(shopacc != null || shop.admin){
					int am = com.getInteger("amount");
					try{
						if(shop.sell) processBuy(am);
						else processSell(am);
						FSMMShop.updateShop(player.entity.getWorld().local(), pos);
						sendSync();
					}
					catch(Exception e){
						e.printStackTrace();
					}
					return;
				}
				return;
			}
			case "sync":{
				if(client){
					bal = com.getLong("bal");
					msg = com.has("msg") ? com.getString("msg") : null;
				}
				else{
					com.set("bal", plyacc.getBalance());
					SEND_TO_CLIENT.accept(com, player);
				}
				return;
			}
		}
	}

	private void processBuy(int am){
		if(!shop.admin && am > shop.stored()){
			msg = "ui.fsmmshop.shop_viewer.sell.not_enough_stored";
			sendSync();
		}
		if(plyacc.getBalance() < am * shop.price){
			msg = "ui.fsmmshop.shop_viewer.sell.not_enough_money";
			sendSync();
		}
		if(shop.admin){
			if(FSConfig.MAXUSEBALANCE > 0 && plyacc.getBalance() > FSConfig.MAXUSEBALANCE){
				player.entity.send(FSConfig.MAXUSEBALMSG);
				return;
			}
			plyacc.modifyBalance(Manageable.Action.SUB, am * shop.price, player.entity);
			while(am > 0){
				StackWrapper stack = shop.stack.copy();
				stack.count(am > stack.count() ? stack.count() : am);
				if(am > stack.count()){
					am -= stack.count();
				}
				else{
					am = 0;
				}
				player.entity.addStack(stack);
			}
		}
		else{
			plyacc.getBank().processAction(Bank.Action.TRANSFER, player.entity, plyacc, am * shop.price, shopacc);
			StackWrapper stack, copy;
			for(int i = 0; i < 9 && am > 0; i++){
				if(shop.stack.equals(stack = shop.inventory.get(i)) && !stack.empty()){
					if(stack.count() > am){
						copy = stack.copy();
						copy.count(am);
						stack.decr(am);
						am = 0;
					}
					else{
						int c = (copy = stack.copy()).count();
						stack.decr(am);
						am -= c;
					}
					player.entity.addStack(copy);
				}
			}
		}
	}

	private void processSell(int am){
		int stored = shop.stored();
		if(!shop.admin && stored + am > shop.limit()){
			msg = "ui.fsmmshop.shop_viewer.buy.not_enough_space";
			sendSync();
			return;
		}
		if(!shop.admin && shopacc.getBalance() < am * shop.price){
			msg = "ui.fsmmshop.shop_viewer.buy.not_enough_money";
			sendSync();
			return;
		}
		if(shop.admin && FSConfig.MAXUSEBALANCE > 0 && plyacc.getBalance() > FSConfig.MAXUSEBALANCE){
			player.entity.send(FSConfig.MAXUSEBALMSG);
			return;
		}
		int found = 0;
		StackWrapper stack;
		for(int i = 0; i < player.entity.getInventorySize(); i++){
			stack = player.entity.getStackAt(i);
			if(shop.stack.equals(stack)) found += stack.count();
		}
		if(found < am){
			msg = "ui.fsmmshop.shop_viewer.buy.not_enough_items";
			sendSync();
			return;
		}
		found = am;
		ArrayList<StackWrapper> stacks = new ArrayList<>();
		for(int i = 0; i < player.entity.getInventorySize(); i++){
			if(found <= 0) break;
			stack = player.entity.getStackAt(i);
			if(shop.stack.equals(stack)){
				StackWrapper copy = stack.copy();
				int count = stack.count();
				if(count > found) copy.count(found);
				stack.decr(found);
				stacks.add(copy);
				found -= count;
			}
		}
		if(shop.admin){
			plyacc.modifyBalance(Manageable.Action.ADD, am * shop.price, player.entity);
		}
		else{
			for(int i = 0; i < stacks.size(); i++){
				stack = stacks.get(i);
				for(int x = 0; x < shop.inventory.size() && !stack.empty(); x++){
					stack = shop.inventory.insert(x, stacks.get(i), false);
				}
			}
			shopacc.getBank().processAction(Bank.Action.TRANSFER, player.entity, shopacc, am * shop.price, plyacc);
		}
	}

	private void sendSync(){
		TagCW com = TagCW.create();
		com.set("task", "sync");
		com.set("bal", plyacc.getBalance());
		if(msg != null) com.set("msg", msg);
		msg = null;
	}

}
