package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.uni.ui.UIUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class GuiHandler implements IGuiHandler {

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return UIUtils.getServer("fsmmshop", ID, player, x, y, z);
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return UIUtils.getClient("fsmmshop", ID, player, x, y, z);
    }

}