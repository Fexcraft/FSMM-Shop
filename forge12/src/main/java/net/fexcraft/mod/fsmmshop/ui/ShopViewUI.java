package net.fexcraft.mod.fsmmshop.ui;

import java.util.ArrayList;

import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.fexcraft.mod.fsmm.util.Config;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopViewUI extends GenericGui<ShopContainer> {

    private static final ResourceLocation texture = new ResourceLocation("fsmmshop:textures/gui/shop_view.png");
    private final ArrayList<String> ttip = new ArrayList<>();
    private final int[] am = new int[]{1, 1, 2, 4, 8, 16, 32, 64};

    public ShopViewUI(EntityPlayer player, World world, int x, int y, int z) {
        super(texture, new ShopContainer(1, player, world, x, y, z), player);
        xSize = 176;
        ySize = 142;
    }

    protected void init() {
        texts.put("title", (new GenericGui.BasicText(guiLeft + 9, guiTop + 9, 146, null, "...")).autoscale().hoverable(true));
        texts.put("amount", (new GenericGui.BasicText(guiLeft + 33, guiTop + 22, 64, Integer.valueOf(MapColor.SNOW.colorValue), "...")).autoscale().hoverable(true));
        texts.put("price", (new GenericGui.BasicText(guiLeft + 103, guiTop + 22, 64, Integer.valueOf(MapColor.SNOW.colorValue), "...")).autoscale().hoverable(true));
        texts.put("balance", (new GenericGui.BasicText(guiLeft + 9, guiTop + 50, 158, Integer.valueOf(MapColor.SNOW.colorValue), "...")).autoscale().hoverable(true));
        String sb = container.tile.shop.sell ? " Buy" : " Sell";
        texts.put("act", (new GenericGui.BasicText(guiLeft + 33, guiTop + 34, 29, Integer.valueOf(MapColor.SNOW.colorValue), sb)).autoscale().hoverable(true));
        for(int i = 0; i < 8; i++){
            final int j = i, u = (i == 0) ? 31 : (51 + i * 15);
            buttons.put("act" + i, new GenericGui.BasicButton("act" + i, guiLeft + u, guiTop + 33, u, 33, (i == 0) ? 33 : 13, 10, true) {
                public boolean onclick(int x, int y, int m) {
                    NBTTagCompound com = new NBTTagCompound();
                    com.setString("cargo", "act");
                    com.setInteger("amount", ShopViewUI.this.am[j]);
                    container.send(Side.SERVER, com);
                    return true;
                }
            });
            buttons.get("act" + i).rgb_none.alpha = 1.0F;
        }
        NBTTagCompound com = new NBTTagCompound();
        com.setString("cargo", "sync");
        container.send(Side.SERVER, com);
    }

    protected void predraw(float pticks, int mouseX, int mouseY) {
        if(container.msg != null){
            texts.get("title").string = Formatter.format(container.msg);
        }
        else if(container.tile.shop.admin){
            texts.get("title").string = "Admin Shop";
        }
        else{
            texts.get("title").string = (container.tile.shop.owner == null) ? "New Shop" : (container.name + "'s Shop");
        }
        if(container.tile.shop.admin){
            texts.get("amount").string = "infinite stock";
        }
        else if(container.tile.shop.sell){
            texts.get("amount").string = container.tile.shop.stored() + " in stock";
        }
        else{
            int l = container.tile.shop.limit();
            texts.get("amount").string = (l - container.tile.shop.stored()) + " of " + l + " free";
        }
        texts.get("price").string = Config.getWorthAsString(container.tile.shop.price);
        texts.get("balance").string = "Your Balance: " + Config.getWorthAsString(container.balance);
    }

    protected void drawlast(float pticks, int mouseX, int mouseY){
        RenderHelper.enableGUIStandardItemLighting();
        itemRender.renderItemAndEffectIntoGUI(container.tile.shop.stack.local(), guiLeft + 10, guiTop + 24);
        RenderHelper.disableStandardItemLighting();
        for(GenericGui.BasicText text : texts.values()){
            if(text.hovered) ttip.add(text.string);
        }
        if(mouseX >= guiLeft + 10 && mouseX <= guiLeft + 26 && mouseY >= guiTop + 24 && mouseY <= guiTop + 40) {
            ttip.add(Formatter.format("&9" + container.tile.shop.stack.getName()));
            ((ItemStack)container.tile.shop.stack.local()).getItem().addInformation(container.tile.shop.stack.local(), player.world, ttip, (ITooltipFlag)ITooltipFlag.TooltipFlags.ADVANCED);
            ttip.add(Formatter.format("&8" + container.tile.shop.stack.getID() + " | " + container.tile.shop.stack.damage()));
        }
        if(ttip.size() > 0) drawHoveringText(ttip, mouseX, mouseY);
        ttip.clear();
    }
}
