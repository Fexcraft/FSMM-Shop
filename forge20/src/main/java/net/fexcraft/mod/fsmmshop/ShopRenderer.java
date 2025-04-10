package net.fexcraft.mod.fsmmshop;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.Renderer120;
import net.fexcraft.mod.fsmm.util.Config;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

import java.util.ArrayList;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer120.AY;
import static net.fexcraft.mod.fcl.util.Renderer120.AZ;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopRenderer implements BlockEntityRenderer<ShopEntity> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fsmmshop:textures/block/shop.png");
	private static ShopModel MODEL = new ShopModel();
	private static RGB sell = new RGB(5887044), buy = new RGB(16539473);
	private static RGB norm = RGB.WHITE.copy();
	private static RGB adm = new RGB(15858708);
	private static Minecraft mc;
	private String text;
	private float s;
	private int w;

	@Override
	public void render(ShopEntity tile, float ticks, PoseStack pose, MultiBufferSource buffer, int light, int overlay){
		Renderer120.pose = pose;
		Renderer120.set(pose, buffer, light, overlay);
		FCLRenderTypes.setCutout(TEXTURE);
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		Direction dir = tile.getBlockState().getValue(FACING);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(dir.getAxis() == Direction.Axis.Z ? dir.toYRot() : dir.toYRot() - 180), AY));
		pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AZ));
		for(ArrayList<ModelRendererTurbo> group : MODEL.groups){
			for(ModelRendererTurbo turbo : group){
				turbo.render();
			}
		}
		if(tile.shop.stack != null && !tile.shop.stack.empty()){
			Renderer120.setColor(tile.shop.sell ? buy : sell);
			ShopModel.top.render();
			Renderer120.setColor(tile.shop.admin ? adm : norm);
			ShopModel.bot.render();
			Renderer120.resetColor();
			pose.mulPose(new Quaternionf().rotateAxis(-Static.rad180, AZ));
			if(mc == null) mc = Minecraft.getInstance();
			pose.translate(0, 0.375, 0);
			mc.getItemRenderer().render(tile.shop.stack.local(), ItemDisplayContext.GROUND, false, pose, buffer, light, overlay,
				mc.getItemRenderer().getModel(tile.shop.stack.local(), tile.getLevel(), null, light));
			pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AY));
			pose.translate(0, -0.2, -0.48);
			draw(pose, buffer, light, overlay, Config.getWorthAsString(tile.shop.price, true, false));
			pose.translate(0, 0.75, 0);
			draw(pose, buffer, light, overlay, tile.shop.sell ? "For Sale" : "Wanted");
		}
		pose.popPose();
	}

	private void draw(PoseStack pose, MultiBufferSource buffer, int light, int overlay, String text){
		w = mc.font.width(text);
		s = 0.0125f * ((w > 48) ? (48f / w) : 1f);
		pose.pushPose();
		pose.scale(-s, -s, s);
		mc.font.drawInBatch(text, -w / 2, 0, 0, false, pose.last().pose(), buffer, Font.DisplayMode.NORMAL, light, overlay);
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

}
