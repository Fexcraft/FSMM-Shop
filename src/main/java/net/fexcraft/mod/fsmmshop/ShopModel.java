package net.fexcraft.mod.fsmmshop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.fexcraft.lib.mc.api.registry.fModel;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fModel(registryname = "fsmmshop:models/block/shop")
public class ShopModel implements FCLBlockModel {

    private static final ArrayList<ArrayList<ModelRendererTurbo>> groups = new ArrayList<>();
    public static ModelRendererTurbo top;
    public static ModelRendererTurbo bot;

    public ShopModel(){
        int textureX = 64;
        int textureY = 64;
        ArrayList<ModelRendererTurbo> supports = new ArrayList<>();
        supports.add((new ModelRendererTurbo(supports, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(1.0F, 14.0F, 1.0F)
                .removePolygons(new int[]{2, 3}).setPolygonUV(1, new float[]{1.0F, 36.0F}).setPolygonUV(0, new float[]{0.0F, 36.0F}).setPolygonUV(5, new float[]{2.0F, 36.0F}).setPolygonUV(4, new float[]{3.0F, 36.0F}).setDetachedUV(new int[]{1, 0, 5, 4}).build()
                .setRotationPoint(-7.5F, -3.0F, -7.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        supports.add((new ModelRendererTurbo(supports, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(1.0F, 14.0F, 1.0F)
                .removePolygons(new int[]{2, 3}).setPolygonUV(1, new float[]{5.0F, 36.0F}).setPolygonUV(0, new float[]{4.0F, 36.0F}).setPolygonUV(5, new float[]{6.0F, 36.0F}).setPolygonUV(4, new float[]{7.0F, 36.0F}).setDetachedUV(new int[]{1, 0, 5, 4}).build()
                .setRotationPoint(-7.5F, -3.0F, 6.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        supports.add((new ModelRendererTurbo(supports, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(1.0F, 14.0F, 1.0F)
                .removePolygons(new int[]{2, 3}).setPolygonUV(1, new float[]{9.0F, 36.0F}).setPolygonUV(0, new float[]{8.0F, 36.0F}).setPolygonUV(5, new float[]{10.0F, 36.0F}).setPolygonUV(4, new float[]{11.0F, 36.0F}).setDetachedUV(new int[]{1, 0, 5, 4}).build()
                .setRotationPoint(6.5F, -3.0F, 6.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        supports.add((new ModelRendererTurbo(supports, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(13.0F, -12.0F, 0.0F).setSize(1.0F, 14.0F, 1.0F)
                .removePolygons(new int[]{2, 3}).setPolygonUV(1, new float[]{13.0F, 36.0F}).setPolygonUV(0, new float[]{12.0F, 36.0F}).setPolygonUV(5, new float[]{14.0F, 36.0F}).setPolygonUV(4, new float[]{15.0F, 36.0F}).setDetachedUV(new int[]{1, 0, 5, 4}).build()
                .setRotationPoint(-6.5F, -3.0F, -7.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        groups.add(supports);
        ArrayList<ModelRendererTurbo> windows = new ArrayList<>();
        windows.add((new ModelRendererTurbo(windows, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(13.0F, 14.0F, 0.0F)
                .removePolygons(new int[]{0, 1, 2, 3}).setPolygonUV(5, new float[]{46.0F, 1.0F}).setPolygonUV(4, new float[]{33.0F, 1.0F}).setDetachedUV(new int[]{5, 4}).build()
                .setRotationPoint(-6.5F, -3.0F, 7.0F).setRotationAngle(0.0F, 0.0F, 0.0F));
        windows.add((new ModelRendererTurbo(windows, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(13.0F, 14.0F, 0.0F)
                .removePolygons(new int[]{0, 1, 2, 3}).setPolygonUV(5, new float[]{46.0F, 1.0F}).setPolygonUV(4, new float[]{33.0F, 1.0F}).setDetachedUV(new int[]{5, 4}).build()
                .setRotationPoint(-6.5F, -3.0F, -7.0F).setRotationAngle(0.0F, 0.0F, 0.0F));
        windows.add((new ModelRendererTurbo(windows, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(0.0F, 14.0F, 13.0F)
                .removePolygons(new int[]{2, 3, 4, 5}).setPolygonUV(1, new float[]{46.0F, 15.0F}).setPolygonUV(0, new float[]{33.0F, 15.0F}).setDetachedUV(new int[]{1, 0}).build()
                .setRotationPoint(7.0F, -3.0F, -6.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        windows.add((new ModelRendererTurbo(windows, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.0F, 0.0F).setSize(0.0F, 14.0F, 13.0F)
                .removePolygons(new int[]{2, 3, 4, 5}).setPolygonUV(1, new float[]{46.0F, 15.0F}).setPolygonUV(0, new float[]{33.0F, 15.0F}).setDetachedUV(new int[]{1, 0}).build()
                .setRotationPoint(-7.0F, -3.0F, -6.5F).setRotationAngle(0.0F, 0.0F, 0.0F));
        groups.add(windows);
        ArrayList<ModelRendererTurbo> top_bot = new ArrayList<>();
        top_bot.add((new ModelRendererTurbo(top_bot, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, 0.0F, 0.0F).setSize(16.0F, 1.0F, 16.0F)
                .setPolygonUV(1, new float[]{16.0F, 32.0F}).setPolygonUV(3, new float[]{0.0F, 0.0F}).setPolygonUV(0, new float[]{0.0F, 32.0F}).setPolygonUV(5, new float[]{32.0F, 32.0F}).setPolygonUV(4, new float[]{48.0F, 32.0F}).setPolygonUV(2, new float[]{16.0F, 0.0F}).setDetachedUV(new int[]{1, 3, 0, 5, 4, 2}).build()
                .setRotationPoint(-8.0F, -1.0F, -8.0F).setRotationAngle(0.0F, 0.0F, 0.0F));
        top_bot.add((new ModelRendererTurbo(top_bot, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -13.0F, 0.0F).setSize(16.0F, 0.5F, 16.0F)
                .setCorners(-1.0F, 0.0F, -1.0F, -1.0F, 0.0F, -1.0F, -1.0F, 0.0F, -1.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                .removePolygons(new int[]{3}).setPolygonUV(1, new float[]{16.0F, 34.0F}).setPolygonUV(0, new float[]{0.0F, 34.0F}).setPolygonUV(5, new float[]{32.0F, 34.0F}).setPolygonUV(4, new float[]{48.0F, 34.0F}).setPolygonUV(2, new float[]{0.0F, 16.0F}).setDetachedUV(new int[]{1, 0, 5, 4, 2}).build()
                .setRotationPoint(-8.0F, -3.0F, -8.0F).setRotationAngle(0.0F, 0.0F, 0.0F));
        top_bot.add((new ModelRendererTurbo(top_bot, -1, -1, textureX, textureY)).newBoxBuilder()
                .setOffset(0.0F, -12.5F, 0.0F).setSize(16.0F, 0.5F, 16.0F)
                .removePolygons(new int[]{2}).setPolygonUV(1, new float[]{16.0F, 33.0F}).setPolygonUV(3, new float[]{16.0F, 16.0F}).setPolygonUV(0, new float[]{0.0F, 33.0F}).setPolygonUV(5, new float[]{32.0F, 33.0F}).setPolygonUV(4, new float[]{48.0F, 33.0F}).setDetachedUV(new int[]{1, 3, 0, 5, 4}).build()
                .setRotationPoint(-8.0F, -3.0F, -8.0F).setRotationAngle(0.0F, 0.0F, 0.0F));
        groups.add(top_bot);
        ArrayList<ModelRendererTurbo> labels = new ArrayList<>();
        labels.add(bot = (new ModelRendererTurbo(labels, -1, -1, textureX, textureY)).newBoxBuilder().setOffset(0.0F, 0.0F, 0.0F).setSize(10.0F, 2.0F, 0.5F).removePolygons(new int[]{3}).setPolygonUV(1, new float[]{11.0F, 52.0F}).setPolygonUV(0, new float[]{0.0F, 52.0F}).setPolygonUV(5, new float[]{1.0F, 52.0F}).setPolygonUV(4, new float[]{1.0F, 54.0F}).setPolygonUV(2, new float[]{1.0F, 51.0F}).setDetachedUV(new int[]{1, 0, 5, 4, 2}).build().setRotationPoint(-5.0F, -3.0F, 7.1F).setRotationAngle(0.0F, 0.0F, 0.0F));
        labels.add(top = (new ModelRendererTurbo(labels, -1, -1, textureX, textureY)).newBoxBuilder().setOffset(0.0F, 0.0F, 0.0F).setSize(10.0F, 2.0F, 0.5F).removePolygons(new int[]{2}).setPolygonUV(1, new float[]{11.0F, 57.0F}).setPolygonUV(3, new float[]{1.0F, 61.0F}).setPolygonUV(0, new float[]{0.0F, 57.0F}).setPolygonUV(5, new float[]{1.0F, 57.0F}).setPolygonUV(4, new float[]{1.0F, 59.0F}).setDetachedUV(new int[]{1, 3, 0, 5, 4}).build().setRotationPoint(-5.0F, -15.0F, 7.1F).setRotationAngle(0.0F, 0.0F, 0.0F));
    }

    public Collection<ModelRendererTurbo> getPolygons(IBlockState state, EnumFacing side, Map<String, String> arguments, long rand){
        ArrayList<ModelRendererTurbo> list = new ArrayList<>();
        for(ArrayList<ModelRendererTurbo> tlist : groups) list.addAll(tlist);
        return list;
    }

}
