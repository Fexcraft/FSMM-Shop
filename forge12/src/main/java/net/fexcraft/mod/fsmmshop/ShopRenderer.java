package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.fsmm.util.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopRenderer extends TileEntitySpecialRenderer<ShopEntity> {

    private static ResourceLocation texture = new ResourceLocation("fsmmshop:textures/blocks/shop.png");
    private static RGB sell = new RGB(5887044), buy = new RGB(16539473);
    private static RGB norm = RGB.WHITE.copy();
    private static RGB adm = new RGB(15858708);
    private int[] rot = new int[]{0, 0, 180, 0, -90, 90};
    private FontRenderer fr;
    private Minecraft mc;
    private float s;
    private int w;

    public void render(ShopEntity tile, double posX, double posY, double posZ, float ticks, int stage, float alpha) {
        if(tile.shop.stack == null || tile.shop.stack.empty()) return;
        if(mc == null) mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL11.glTranslated(posX + 0.5D, posY, posZ + 0.5D);
        GL11.glPushMatrix();
        GL11.glRotatef(this.rot[tile.getBlockMetadata()], 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        (tile.shop.sell ? buy : sell).glColorApply();
        ShopModel.top.render();
        (tile.shop.admin ? adm : norm).glColorApply();
        ShopModel.bot.render();
        GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslated(0.0D, 0.375D, 0.0D);
        GL11.glRotatef(tile.rot += ticks, 0.0F, 1.0F, 0.0F);
        mc.getItemRenderer().renderItem(mc.player, tile.shop.stack.local(), ItemCameraTransforms.TransformType.GROUND);
        GL11.glRotatef(-tile.rot, 0.0F, 1.0F, 0.0F);
        GL11.glTranslated(0.0D, -0.2D, 0.48D);
        GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
        drawString(Config.getWorthAsString(tile.shop.price, true, false));
        GL11.glTranslated(0.0D, 0.75D, 0.0D);
        drawString(tile.shop.sell ? "For Sale" : "Wanted");
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public final void drawString(String str) {
        if(fr == null) fr = mc.fontRenderer;
        RGB.BLACK.glColorApply();
        GlStateManager.pushMatrix();
        w = fr.getStringWidth(str);
        s = 0.0125F * ((this.w > 48) ? (48.0F / this.w) : 1.0F);
        GlStateManager.scale(-this.s, -this.s, this.s);
        fr.drawString(str, -this.w / 2, 0, 0);
        GlStateManager.popMatrix();
    }

}
