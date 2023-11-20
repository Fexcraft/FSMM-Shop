package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.mc.render.FCLBlockModelLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Ferdinand Calo' (FEX___96)
 *
 */
@Mod(modid = "fsmmshop", name = "FSMM Shop", version = "1.0.2", dependencies = "after:fsmm")
public class FSMMShop {

    @Instance("fsmmshop")
    public static FSMMShop INSTANCE;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws InstantiationException, IllegalAccessException {
        new FCLRegistry.AutoRegisterer("fsmmshop");
        if(event.getSide().isClient()){
            FCLBlockModelLoader.addBlockModel(new ResourceLocation("fsmmshop:models/block/shop"), ((Class<FCLBlockModel>)FCLRegistry.getModel("fsmmshop:models/block/shop")).newInstance());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        if(event.getSide().isClient()) regtileren();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
    }

    @SideOnly(Side.CLIENT)
    private void regtileren(){
        ClientRegistry.bindTileEntitySpecialRenderer(ShopEntity.class, new ShopRenderer());
    }

}
