package net.fexcraft.mod.fsmmshop;

import javax.annotation.Nullable;

import net.fexcraft.lib.mc.api.packet.IPacketReceiver;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.lib.mc.utils.ApiUtil;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEntity extends TileEntity implements IPacketReceiver<PacketTileEntityUpdate> {

	public final Shop shop = new Shop();
	protected float rot;

	public ShopEntity(){}

	public ShopEntity(World world){}

	public void processClientPacket(PacketTileEntityUpdate pkt){
		readFromNBT(pkt.nbt);
		Print.debug("received " + pkt.nbt);
	}

	@Override
	public NBTTagCompound getUpdateTag(){
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(getPos(), 2, writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		shop.write(TagCW.wrap(tag));
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		shop.read(TagCW.wrap(tag));
	}

	public void updateClient(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		ApiUtil.sendTileEntityUpdatePacket(this, compound, 256);
		Print.debug("sending " + compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
		return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing));
	}

	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return shop.inventory.cast();
		return super.getCapability(capability, facing);
	}

	public void dropItems(){
		for(int i = 0; i < shop.inventory.size(); i++){
			StackWrapper stack = shop.inventory.get(i);
			if(stack.empty()) continue;
			EntityItem item = new EntityItem(world);
			item.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			item.setItem(stack.local());
			world.spawnEntity(item);
		}
		shop.inventory.clear();
	}

}
