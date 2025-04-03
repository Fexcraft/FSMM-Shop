package net.fexcraft.mod.fsmmshop;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.fsmm.FSMM;
import net.fexcraft.mod.fsmm.util.FSMMSubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * @author Ferdinand Calo'
 */
public class ShopSubCmd implements FSMMSubCommand {

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args){
        boolean op = sender instanceof EntityPlayer ? server.isSinglePlayer() ? true : PermissionAPI.hasPermission((EntityPlayer)sender, "fsmm.admin") : true;
        if(!op){
            Print.chat(sender, "&cNo Permission.");
            return;
        }
        if(args.length > 1 && args[1].equals("reload")){
            FSMMShop.CONFIG.reload();
            Print.chat(sender, "&aConfig reloaded.");
        }
		else Print.chat(sender, "&cUnknown Argument. Try &7/fsmm help");
    }

    @Override
	public void printHelp(ICommandSender sender){
        Print.chat(sender, "&eShop commands:");
        Print.chat(sender, "&7/fsmm shop reload");
    }

    @Override
	public void printVersion(ICommandSender sender){
        Print.chat(sender,"&bFSMM-Shop Version: &e" + FSMMShop.VERSION + "&0.");
    }

}
