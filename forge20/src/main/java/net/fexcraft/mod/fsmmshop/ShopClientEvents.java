package net.fexcraft.mod.fsmmshop;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = FSMMShop.MODID, bus = MOD, value = Dist.CLIENT)
public class ShopClientEvents {

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(FSMMShop.SHOP_ENT.get(), con -> new ShopRenderer());
    }

    @SubscribeEvent
    public static void register(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES || event.getTabKey() == CreativeModeTabs.INVENTORY){
            event.accept(FSMMShop.SHOP_ITEM);
        }
    }

}
