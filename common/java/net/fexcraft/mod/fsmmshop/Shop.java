package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniInventory;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Shop {

	public final UniInventory inventory = UniInventory.create(9);
	public StackWrapper stack = StackWrapper.EMPTY;
	public String oname;
	public UUID owner;
	public boolean admin;
	public boolean sell;
	public long price;

	public void write(TagCW com){
		TagCW tag = TagCW.create();
		stack.save(tag);
		com.set("stack", tag);
		if(this.owner != null){
			com.set("owner-0", owner.getMostSignificantBits());
			com.set("owner-1", owner.getLeastSignificantBits());
			com.set("oname", oname);
		}
		com.set("admin", admin);
		TagLW list = TagLW.create();
		for(int i = 0; i < 9; i++){
			tag = TagCW.create();
			inventory.get(i).save(tag);
			list.add(tag);
		}
		com.set("stacks", list);
		com.set("price", price);
		com.set("sell", sell);
	}

	public void read(TagCW com){
		stack = com.has("stack") ? UniStack.STACK_GETTER.apply(com.getCompound("stack")) : StackWrapper.EMPTY;
		if(com.has("owner-0")){
			owner = new UUID(com.getLong("owner-0"), com.getLong("owner-1"));
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
			if(inventory.get(i).equals(stack)) stored += stack.count();
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

}
