package net.fexcraft.mod.fsmmshop.ui;

import java.util.ArrayList;

import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fsmm.data.Account;
import net.fexcraft.mod.fsmm.data.Bank;
import net.fexcraft.mod.fsmm.data.Manageable;
import net.fexcraft.mod.fsmm.util.DataManager;
import net.fexcraft.mod.fsmmshop.FSConfig;
import net.fexcraft.mod.fsmmshop.FSMMShop;
import net.fexcraft.mod.fsmmshop.ShopEntity;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopContainer extends GenericContainer {

    protected ShopEntity tile;
    protected String name;
    protected String msg;
    protected Account account;
    protected Account tileacc;
    protected long balance;

    public ShopContainer(int id, EntityPlayer player, World world, int x, int y, int z){
        super(player);
        tile = (ShopEntity)world.getTileEntity(new BlockPos(x, y, z));
        if(id == 0){
            for (int i = 0; i < 9; i++){
                addSlotToContainer(new SlotItemHandler(tile.shop.inventory.cast(), i, 8 + i * 18, 46));
            }
        }
        int y0 = (id == 0) ? 70 : 62, y1 = (id == 0) ? 126 : 118;
        for(int row = 0; row < 3; row++){
            for(int i = 0; i < 9; i++){
                addSlotToContainer(new Slot(player.inventory, i + row * 9 + 9, 8 + i * 18, y0 + row * 18));
            }
        }
        for(int col = 0; col < 9; col++){
            addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, y1));
        }
        if(!world.isRemote && tile.shop.owner != null) name = Static.getPlayerNameByUUID(tile.shop.owner);
        if(world.isRemote) return;
        account = DataManager.getAccount("player:" + player.getGameProfile().getId().toString(), false, true);
        if(tile.shop.owner != null) tileacc = DataManager.getAccount("player:" + tile.shop.owner, true, true);
    }

    protected void packet(Side side, NBTTagCompound packet, EntityPlayer player) {
        if(side == Side.SERVER){
            EntityW ms = UniEntity.getEntity(player);
            NBTTagCompound com;
            switch(packet.getString("cargo")){
                case "sync":
                    com = new NBTTagCompound();
                    com.setString("name", name);
                    com.setLong("balance", account.getBalance());
                    send(Side.CLIENT, com);
                    return;
                case "act":
                    if(tileacc != null || tile.shop.admin){
                        int am = packet.getInteger("amount");
                        int stored = tile.shop.stored();
                        if(tile.shop.sell){
                            if(!tile.shop.admin && am > stored){
                                sendSync("&enot enough in stock");
                                return;
                            }
                            if(account.getBalance() < am * tile.shop.price){
                                sendSync("&enot enough money");
                                return;
                            }
                            if(tile.shop.admin){
                                if(FSConfig.MAXUSEBALANCE > 0 && account.getBalance() > FSConfig.MAXUSEBALANCE){
                                    Print.chat(player, FSConfig.MAXUSEBALMSG);
                                    return;
                                }
                                account.modifyBalance(Manageable.Action.SUB, am * tile.shop.price, ms);
                                while(am > 0){
                                    ItemStack stack = tile.shop.stack.copy().local();
                                    stack.setCount((am > stack.getCount()) ? stack.getCount() : am);
                                    if(am > stack.getCount()){
                                        am -= stack.getCount();
                                    }
                                    else{
                                        am = 0;
                                    }
                                    player.inventory.addItemStackToInventory(stack);
                                }
                            }
                            else{
                                this.account.getBank().processAction(Bank.Action.TRANSFER, ms, this.account, am * tile.shop.price, tileacc);
                                StackWrapper stack = null, copy = null;
                                for(int i = 0; i < 9 && am > 0; i++){
                                    if(tile.shop.stack.equals(stack = tile.shop.inventory.get(i)) && !stack.empty()){
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
                                        player.inventory.addItemStackToInventory(copy.local());
                                    }
                                }
                            }
                        }
                        else{
                            if(!tile.shop.admin && stored + am > tile.shop.limit()){
                                sendSync("&enot enough shop storage");
                                return;
                            }
                            if(!tile.shop.admin && tileacc.getBalance() < am * tile.shop.price){
                                sendSync("&eshop out of money");
                                return;
                            }
                            if(tile.shop.admin && FSConfig.MAXUSEBALANCE > 0 && account.getBalance() > FSConfig.MAXUSEBALANCE){
                                Print.chat(player, FSConfig.MAXUSEBALMSG);
                                return;
                            }
                            int found = 0;
                            for(ItemStack stack : player.inventory.mainInventory){
                                if(tile.shop.stack.equals(UniStack.getStack(stack))) found += stack.getCount();
                            }
                            if(found < am){
                                sendSync("&eyou do not have enough items");
                                return;
                            }
                            found = am;
                            ArrayList<ItemStack> stacks = new ArrayList<>();
                            for(ItemStack stack : player.inventory.mainInventory){
                                if(found <= 0) break;
                                if(tile.shop.stack.equals(UniStack.getStack(stack))){
                                    ItemStack copy = stack.copy();
                                    int count = stack.getCount();
                                    if(count > found) copy.setCount(found);
                                    stack.shrink(found);
                                    stacks.add(copy);
                                    found -= count;
                                }
                            }
                            if(tile.shop.admin){
                                account.modifyBalance(Manageable.Action.ADD, am * tile.shop.price, ms);
                            }
                            else{
                                for(int i = 0; i < stacks.size(); i++){
                                    ItemStack stack = stacks.get(i);
                                    for(int x = 0; x < 9 && !stack.isEmpty(); x++)
                                        stack = ((IItemHandler)tile.shop.inventory).insertItem(x, stacks.get(i), false);
                                }
                                tileacc.getBank().processAction(Bank.Action.TRANSFER, ms, tileacc, am * tile.shop.price, this.account);
                            }
                        }
                        tile.updateClient();
                        sendSync(null);
                        return;
                    }
                    sendSync(null);
            }
        }
        else{
            if(packet.hasKey("name")) name = packet.getString("name");
            balance = packet.getLong("balance");
            msg = packet.hasKey("message") ? packet.getString("message") : null;
        }
    }

    private void sendSync(String msg){
        NBTTagCompound com = new NBTTagCompound();
        com.setLong("balance", this.account.getBalance());
        if(msg != null) com.setString("message", msg);
        send(Side.CLIENT, com);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index){
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 9){
                if(!mergeItemStack(itemstack1, 9, inventorySlots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }
            else if(!mergeItemStack(itemstack1, 0, 9, false)){
                return ItemStack.EMPTY;
            }
            if(itemstack1.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            }
            else{
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
}
