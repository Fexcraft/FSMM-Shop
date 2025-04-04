package net.fexcraft.mod.fsmmshop.ui;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fsmm.util.Config;
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
public class ShopEditUI extends GenericGui<ShopContainer> {
    
    private static final ResourceLocation texture = new ResourceLocation("fsmmshop:textures/gui/shop_edit.png");
    private final ArrayList<String> ttip = new ArrayList<>();

    public ShopEditUI(EntityPlayer player, World world, int x, int y, int z){
        super(texture, new ShopContainer(0, player, world, x, y, z), player);
        this.xSize = 176;
        this.ySize = 150;
    }

    protected void init(){
        texts.put("title", (new GenericGui.BasicText(guiLeft + 9, guiTop + 9, 146, null, "...")).autoscale().hoverable(true));
        fields.put("price", new GenericGui.TextField(0, fontRenderer, guiLeft + 31, guiTop + 21, 126, 10));
        texts.put("sell", (new GenericGui.BasicText(guiLeft + 32, guiTop + 34, 66, Integer.valueOf(14606046), "   Sell Mode")).autoscale().hoverable(true));
        texts.put("buy", (new GenericGui.BasicText(guiLeft + 102, guiTop + 34, 66, Integer.valueOf(14606046), "   Buy Mode")).autoscale().hoverable(true));
        buttons.put("admin", new GenericGui.BasicButton("admin", guiLeft + 159, guiTop + 8, 159, 8, 10, 10, true) {
            public boolean onclick(int x, int y, int m) {
                NBTTagCompound com = new NBTTagCompound();
                com.setString("cargo", "adminmode");
                container.send(Side.SERVER, com);
                return true;
            }
        });
        buttons.put("confirm", new GenericGui.BasicButton("confirm", guiLeft + 159, guiTop + 21, 159, 21, 10, 10, true) {
            public boolean onclick(int x, int y, int m) {
                NBTTagCompound com = new NBTTagCompound();
                com.setString("cargo", "price");
                com.setLong("price", format());
                container.send(Side.SERVER, com);
                return true;
            }
        });
        buttons.put("sell", new GenericGui.BasicButton("sell", guiLeft + 31, guiTop + 33, 31, 33, 68, 10, true) {
            public boolean onclick(int x, int y, int m) {
                NBTTagCompound com = new NBTTagCompound();
                com.setString("cargo", "sellmode");
                container.send(Side.SERVER, com);
                return true;
            }
        });
        buttons.put("buy", new GenericGui.BasicButton("buy", guiLeft + 101, guiTop + 33, 101, 33, 68, 10, true) {
            public boolean onclick(int x, int y, int m) {
                NBTTagCompound com = new NBTTagCompound();
                com.setString("cargo", "buymode");
                container.send(Side.SERVER, com);
                return true;
            }
        });
        buttons.put("put", new GenericGui.BasicButton("buy", guiLeft + 10, guiTop + 24, 10, 24, 16, 16, true) {
            public boolean onclick(int x, int y, int m) {
                NBTTagCompound com = new NBTTagCompound();
                com.setString("cargo", "setstack");
                container.send(Side.SERVER, com);
                return true;
            }
        });
        buttons.get("buy").rgb_hover = new RGB(244, 215, 66, 1.0F);
        buttons.get("buy").rgb_none = (new RGB("#ffffff")).setAlpha(1.0F);
        fields.get("price").setText(Config.getWorthAsString(container.tile.shop.price, false));
        NBTTagCompound com = new NBTTagCompound();
        com.setString("cargo", "sync");
        container.send(Side.SERVER, com);
    }

    protected void predraw(float pticks, int mouseX, int mouseY) {
        buttons.get("sell").tx = container.tile.shop.sell ? 31 : 101;
        buttons.get("buy").tx = container.tile.shop.sell ? 101 : 31;
        if(container.tile.shop.admin){
            texts.get("title").string = "Admin Shop";
        }
        else{
            texts.get("title").string = (container.tile.shop.owner == null) ? "New Shop" : (container.name + "'s Shop");
        }
    }

    protected void drawlast(float pticks, int mouseX, int mouseY) {
        RenderHelper.enableGUIStandardItemLighting();
        itemRender.renderItemAndEffectIntoGUI(container.tile.shop.stack.local(), guiLeft + 10, guiTop + 24);
        RenderHelper.disableStandardItemLighting();
        if(buttons.get("admin").hovered){
            ttip.add(Formatter.format("&6Admin Shop is " + (container.tile.shop.admin ? "&cactive" : "&ainactive") + "&6."));
            ttip.add(Formatter.format("&bClick to toggle."));
        }
        if(buttons.get("confirm").hovered) ttip.add(Formatter.format("&bClick to confirm new price."));
        for(GenericGui.BasicText text : texts.values()){
            if(text.hovered) ttip.add(text.string);
        }
        if(mouseX >= guiLeft + 10 && mouseX <= guiLeft + 26 && mouseY >= guiTop + 24 && mouseY <= guiTop + 40){
            ttip.add(Formatter.format("&9" + container.tile.shop.stack.getName()));
            ((ItemStack)container.tile.shop.stack.direct()).getItem().addInformation(container.tile.shop.stack.local(), player.world, ttip, (ITooltipFlag) ITooltipFlag.TooltipFlags.ADVANCED);
            ttip.add(Formatter.format("&8" + container.tile.shop.stack.getID() + " | " + container.tile.shop.stack.damage()));
        }
        if(ttip.size() > 0) drawHoveringText(ttip, mouseX, mouseY);
        ttip.clear();
    }

    private static final DecimalFormat df = new DecimalFormat("#.000", new DecimalFormatSymbols(Locale.US));

    static {
        df.setRoundingMode(RoundingMode.DOWN);
    }

    private final long format() {
        try{
            String str = fields.get("price").getText().replace(Config.DOT, "").replace(",", ".");
            if(str.length() == 0) return 0;
            String format = df.format(Double.parseDouble(str));
            return Long.parseLong(format.replace(",", "").replace(".", ""));
        } catch (Exception e) {
            Print.chat(this.player, "INVALID INPUT: " + e.getMessage());
            if(Static.dev()) e.printStackTrace();
            return 0;
        }
    }

}
