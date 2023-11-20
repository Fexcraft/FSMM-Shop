package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.mc.api.registry.fRecipeHolder;
import net.fexcraft.lib.mc.crafting.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fRecipeHolder("fsmmshop")
public class RecipeHolder {

    public RecipeHolder(){
        Ingredient ingot = Ingredient.fromStacks(new ItemStack[]{new ItemStack(Items.IRON_INGOT)});
        Ingredient glass = Ingredient.fromStacks(new ItemStack[]{new ItemStack(Blocks.GLASS)});
        RecipeRegistry.addShapedRecipe("fsmmshop:shop", null, new ItemStack((Block) ShopBlock.INST), 3, 3, new Ingredient[]{Ingredient.EMPTY, ingot, Ingredient.EMPTY, ingot, glass, ingot, ingot, ingot, ingot});
    }

}