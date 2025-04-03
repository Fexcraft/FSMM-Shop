package net.fexcraft.mod.fsmmshop;

import net.fexcraft.lib.mc.api.registry.fModel;
import net.fexcraft.lib.mc.render.FCLBlockModel;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fModel(registryname = "fsmmshop:models/block/shop")
public class ShopModel12 extends ShopModel implements FCLBlockModel {

	@Override
	public Collection<ModelRendererTurbo> getPolygons(IBlockState state, EnumFacing side, Map<String, String> arguments, long rand){
		ArrayList<ModelRendererTurbo> list = new ArrayList<>();
		for(ArrayList<ModelRendererTurbo> tlist : groups) list.addAll(tlist);
		return list;
	}

}
