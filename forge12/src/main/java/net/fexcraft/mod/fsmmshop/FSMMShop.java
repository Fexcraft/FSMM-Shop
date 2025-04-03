package net.fexcraft.mod.fsmmshop;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.mc.crafting.RecipeRegistry;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.mc.render.FCLBlockModelLoader;
import net.fexcraft.mod.fsmm.util.FSMMSubCommand;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
    public static final String VERSION = "2.0";
    //
    public static FSConfig CONFIG;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws InstantiationException, IllegalAccessException {
        new FCLRegistry.AutoRegisterer("fsmmshop");
        if(event.getSide().isClient()){
            FCLBlockModelLoader.addBlockModel(new ResourceLocation("fsmmshop:models/block/shop"), ((Class<FCLBlockModel>)FCLRegistry.getModel("fsmmshop:models/block/shop")).newInstance());
        }
        CONFIG = new FSConfig(new File(event.getSuggestedConfigurationFile().getParentFile(), "fsmm-shop.json"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        if(event.getSide().isClient()) regtileren();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        FSMMSubCommand.register("shop", new ShopSubCmd());
        //
        Ingredient ingot = Ingredient.fromStacks(new ItemStack[]{new ItemStack(Items.IRON_INGOT)});
        Ingredient glass = Ingredient.fromStacks(new ItemStack[]{new ItemStack(Blocks.GLASS)});
        RecipeRegistry.addShapedRecipe("fsmmshop:shop", null, new ItemStack((Block) ShopBlock.INST), 3, 3, new Ingredient[]{Ingredient.EMPTY, ingot, Ingredient.EMPTY, ingot, glass, ingot, ingot, ingot, ingot});
    }

    @SideOnly(Side.CLIENT)
    private void regtileren(){
        ClientRegistry.bindTileEntitySpecialRenderer(ShopEntity.class, new ShopRenderer());
    }

}
