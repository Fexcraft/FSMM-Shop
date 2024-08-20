package net.fexcraft.mod.fsmmshop;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.mc.render.FCLBlockModelLoader;
import net.fexcraft.mod.fsmm.util.Command;
import net.fexcraft.mod.fsmm.util.FSMMSubCommand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
@Mod(modid = "fsmmshop", name = "FSMM Shop", version = FSMMShop.VERSION, dependencies = "after:fsmm")
public class FSMMShop {

    @Instance("fsmmshop")
    public static FSMMShop INSTANCE;
    public static final String VERSION = "1.1.0";
    //
    public static File CFGFILE;
    public static int MAXUSEBALANCE;
    public static String MAXUSEBALMSG;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws InstantiationException, IllegalAccessException {
        new FCLRegistry.AutoRegisterer("fsmmshop");
        if(event.getSide().isClient()){
            FCLBlockModelLoader.addBlockModel(new ResourceLocation("fsmmshop:models/block/shop"), ((Class<FCLBlockModel>)FCLRegistry.getModel("fsmmshop:models/block/shop")).newInstance());
        }
        CFGFILE = new File(event.getSuggestedConfigurationFile().getParentFile(), "fsmm_shop.json");
        if(!CFGFILE.exists()){
            JsonMap map = new JsonMap();
            map.add("max-use-balance", MAXUSEBALANCE = 0);
            map.add("max-use-balance-msg", MAXUSEBALMSG = "&eYour balance is too high to use admin shops.");
            JsonHandler.print(CFGFILE, map, JsonHandler.PrintOption.DEFAULT);
        }
        else{
            JsonMap map = JsonHandler.parse(CFGFILE);
            MAXUSEBALANCE = map.getInteger("max-use-balance", 0);
            MAXUSEBALMSG = map.getString("max-use-balance-msg", "&eYour balance is too high to use admin shops.");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        if(event.getSide().isClient()) regtileren();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        FSMMSubCommand.register("shop", new ShopSubCmd());
    }

    @SideOnly(Side.CLIENT)
    private void regtileren(){
        ClientRegistry.bindTileEntitySpecialRenderer(ShopEntity.class, new ShopRenderer());
    }

}
