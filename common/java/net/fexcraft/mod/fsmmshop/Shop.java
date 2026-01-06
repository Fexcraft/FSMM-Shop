package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.fsmm.data.Account;
import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.fsmm.util.DataManager;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Shop {

	public final UniInventory inventory = UniInventory.create(9);
	public StackWrapper stack = StackWrapper.EMPTY;
	public String owner;
	public String oname;
	public boolean admin;
	public boolean sell;
	public long price;

	public void write(TagCW com){
		if(!stack.empty()){
			TagCW tag = TagCW.create();
			stack.save(tag);
			com.set("stack", tag);
		}
		if(owner != null){
			com.set("owner", owner);
			if(oname != null) com.set("oname", oname);
		}
		com.set("admin", admin);
		TagLW list = TagLW.create();
		for(int i = 0; i < 9; i++){
			TagCW tag = TagCW.create();
			inventory.get(i).save(tag);
			list.add(tag);
		}
		com.set("stacks", list);
		com.set("price", price);
		com.set("sell", sell);
	}

	public void read(TagCW com){
		stack = com.has("stack") ? UniStack.STACK_GETTER.apply(com.getCompound("stack")) : StackWrapper.EMPTY;
		if(com.has("owner")){
			owner = com.getString("owner");
			oname = com.has("oname") ? com.getString("oname") : owner.toString();
		}
		admin = com.getBoolean("admin");
		inventory.clear();
		if(com.has("stacks")){
			TagLW list = com.getList("stacks");
			for(int i = 0; i < 9; i++) inventory.set(i, UniStack.STACK_GETTER.apply(list.getCompound(i)));
		}
		price = com.getLong("price");
		sell = com.getBoolean("sell");
	}

	public int stored(){
		int stored = 0;
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).equals(stack)) stored += inventory.get(i).count();
		}
		return stored;
	}

	public int limit(){
		int limit = 0;
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).empty()){
				limit += stack.maxsize();
			}
			if(inventory.get(i).equals(stack)){
				limit += Math.min(inventory.get(i).maxsize(), stack.maxsize());
			}
		}
		return Math.min(limit, 576);
	}

	public void setOwner(UniEntity entity){
		PlayerAccData data = entity.getApp(PlayerAccData.class);
		if(data.getSelectedAccount() == null){
			owner = data.getAccount().getTypeAndId();
			oname = entity.entity.getName();
		}
		else{
			owner = data.getSelectedAccount().getTypeAndId();
			oname = data.getSelectedAccount().getAccount().getName();
		}
	}

	public Account account(){
		if(owner == null || admin) return null;
		return DataManager.getAccount(owner, 2);
	}

	public boolean isOwner(PlayerAccData data){
		if(data.getSelectedAccount() == null){
			return owner.equals(data.getAccount().getTypeAndId());
		}
		else return owner.equals(data.getSelectedAccount().getTypeAndId());
	}
}
