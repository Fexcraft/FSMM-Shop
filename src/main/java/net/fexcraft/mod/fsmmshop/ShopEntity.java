package net.fexcraft.mod.fsmmshop;

import java.util.UUID;
import javax.annotation.Nullable;

import net.fexcraft.lib.mc.api.packet.IPacket;
import net.fexcraft.lib.mc.api.packet.IPacketReceiver;
import net.fexcraft.lib.mc.network.packet.PacketTileEntityUpdate;
import net.fexcraft.lib.mc.utils.ApiUtil;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEntity extends TileEntity implements IPacketReceiver<PacketTileEntityUpdate> {

    private final NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    public ItemStackHandler handler = new ItemStackHandler(stacks);
    public ItemStack stack = ItemStack.EMPTY;
    public UUID owner;
    public boolean admin;
    public boolean sell;
    public long price;
    protected float rot;

    public ShopEntity(){}

    public ShopEntity(World world){}

    public void processServerPacket(PacketTileEntityUpdate pkt){}

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
        tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        if(this.owner != null){
            tag.setLong("owner-0", owner.getMostSignificantBits());
            tag.setLong("owner-1", owner.getLeastSignificantBits());
        }
        tag.setBoolean("admin", admin);
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < 9; ){
            list.appendTag(stacks.get(i).writeToNBT(new NBTTagCompound()));
            i++;
        }
        tag.setTag("stacks", list);
        tag.setLong("price", price);
        tag.setBoolean("sell", sell);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        if(tag.hasKey("stack")) stack = new ItemStack(tag.getCompoundTag("stack"));
        if(tag.hasKey("owner-0")) owner = new UUID(tag.getLong("owner-0"), tag.getLong("owner-1"));
        admin = tag.getBoolean("admin");
        stacks.clear();
        if(tag.hasKey("stacks")){
            NBTTagList list = (NBTTagList)tag.getTag("stacks");
            for(int i = 0; i < 9; i++) stacks.set(i, new ItemStack(list.getCompoundTagAt(i)));
        }
        price = tag.getLong("price");
        sell = tag.getBoolean("sell");
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
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.handler;
        return super.getCapability(capability, facing);
    }

    public NonNullList<ItemStack> stacks() {
        return stacks;
    }

    public int stored(){
        int stored = 0;
        for(ItemStack is : stacks){
            if(equal(is)) stored += is.getCount();
        }
        return stored;
    }

    public boolean equal(ItemStack other){
        if(stack.getItem() != other.getItem()) return false;
        if(stack.getMetadata() != other.getMetadata()) return false;
        if(stack.getTagCompound() == null && other.getTagCompound() != null) return false;
        return ((stack.getTagCompound() == null || stack.getTagCompound().equals(other.getTagCompound())) && stack.areCapsCompatible(other));
    }

    public int limit(){
        int limit = 0;
        for(ItemStack is : stacks){
            if(is.isEmpty()){
                limit += stack.getMaxStackSize();
                continue;
            }
            if(equal(is)) limit += (is.getMaxStackSize() > 64) ? 64 : is.getMaxStackSize();
        }
        return (limit > 576) ? 576 : limit;
    }

    public void dropItems(){
        for(ItemStack stack : stacks) {
            if(stack.isEmpty()) return;
            EntityItem item = new EntityItem(world);
            item.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            item.setItem(stack);
            world.spawnEntity(item);
        }
        stacks.clear();
    }

}
