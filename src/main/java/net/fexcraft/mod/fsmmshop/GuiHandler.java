package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.fsmmshop.ui.ShopContainer;
import net.fexcraft.mod.fsmmshop.ui.ShopEditUI;
import net.fexcraft.mod.fsmmshop.ui.ShopViewUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class GuiHandler implements IGuiHandler {

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID < 2) return new ShopContainer(ID, player, world, x, y, z);
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0) return new ShopEditUI(player, world, x, y, z);
        if(ID == 1) return new ShopViewUI(player, world, x, y, z);
        return null;
    }

}