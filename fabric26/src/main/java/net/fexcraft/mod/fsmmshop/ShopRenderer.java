package net.fexcraft.mod.fsmmshop;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.mod.fcl.util.FCLRenderTypes;
import net.fexcraft.mod.fcl.util.FCLRenderUtil;
import net.fexcraft.mod.fcl.util.Renderer26;
import net.fexcraft.mod.fsmm.util.Config;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.io.IOException;

import static net.fexcraft.mod.fcl.local.CraftingBlock.FACING;
import static net.fexcraft.mod.fcl.util.Renderer26.AY;
import static net.fexcraft.mod.fcl.util.Renderer26.AZ;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopRenderer implements BlockEntityRenderer<ShopEntity, ShopRenderer.ShopRenderState> {

	public static final IDL TEXTURE = IDLManager.getIDLCached("fsmmshop:textures/block/shop.png");
	private static ShopModel MODEL = new ShopModel();
	private static RGB sell = new RGB(5887044), buy = new RGB(16539473);
	private static RGB norm = RGB.WHITE.copy();
	private static RGB adm = new RGB(15858708);
	private static Minecraft mc;
	private ItemModelResolver resolver;
	private float s;
	private int w;

	public ShopRenderer(BlockEntityRendererProvider.Context context){
		resolver = context.itemModelResolver();
		ShopModel.init(() -> {
			try{
				return Minecraft.getInstance().getResourceManager().getResource(Identifier.parse("fsmmshop:models/block/shop.bob")).get().open();
			}
			catch(IOException e){
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public ShopRenderState createRenderState(){
		return new ShopRenderState();
	}

	@Override
	public void extractRenderState(ShopEntity ent, ShopRenderState state, float ticks, Vec3 cam, ModelFeatureRenderer.CrumblingOverlay progress){
		BlockEntityRenderer.super.extractRenderState(ent, state, ticks, cam, progress);
		state.shop = ent.shop;
		if(state.shop.stack != null){
			resolver.updateForTopItem(state.istate, ent.shop.stack.local(), ItemDisplayContext.GROUND, ent.getLevel(), null, 0);
		}
	}

	@Override
	public void submit(ShopRenderState state, PoseStack pose, SubmitNodeCollector nodecoll, CameraRenderState camera){
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		Direction dir = state.blockState.getValue(FACING);
		pose.mulPose(new Quaternionf().rotateAxis(Static.toRadians(dir.getAxis() == Direction.Axis.Z ? dir.toYRot() : dir.toYRot() - 180), AY));
		pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AZ));
		FCLRenderUtil.set(pose, nodecoll, FCLRenderTypes.getCutout(TEXTURE), state.lightCoords);
		FCLRenderUtil.render(ShopModel.MODEL);
		if(state.shop.stack != null && !state.shop.stack.empty()){
			Renderer26.setColor(state.shop.sell ? buy : sell);
			FCLRenderUtil.render(ShopModel.top);
			Renderer26.setColor(state.shop.admin ? adm : norm);
			FCLRenderUtil.render(ShopModel.bot);
			Renderer26.resetColor();
			pose.mulPose(new Quaternionf().rotateAxis(-Static.rad180, AZ));
			if(mc == null) mc = Minecraft.getInstance();
			pose.translate(0, 0.375, 0);
			state.istate.submit(pose, nodecoll, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			pose.mulPose(new Quaternionf().rotateAxis(Static.rad180, AY));
			pose.translate(0, -0.2, -0.48);
			draw(pose, nodecoll, state.lightCoords, Config.getWorthAsString(state.shop.price, true, false));
			pose.translate(0, 0.75, 0);
			draw(pose, nodecoll, state.lightCoords, state.shop.sell ? "For Sale" : "Wanted");
		}
		pose.popPose();
	}

	private void draw(PoseStack pose, SubmitNodeCollector noco, int light, String text){
		w = mc.font.width(text);
		s = 0.0125f * ((w > 48) ? (48f / w) : 1f);
		pose.pushPose();
		pose.scale(-s, -s, s);
		noco.submitText(pose, -w / 2, 0, Component.literal(text).getVisualOrderText(),
			false, Font.DisplayMode.NORMAL, light, 0xff010101, 0, 0);
		pose.popPose();
	}

	@Override
	public int getViewDistance(){
        return 128;
    }

	public static class ShopRenderState extends BlockEntityRenderState {

		public ItemStackRenderState istate = new ItemStackRenderState();
		public Shop shop;

	}

}
