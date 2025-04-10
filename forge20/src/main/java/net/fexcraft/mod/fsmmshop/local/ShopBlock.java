package net.fexcraft.mod.fsmmshop.local;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fsmm.util.FsmmUIKeys;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopBlock extends Block {

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

	public ShopBlock(){
		super(Properties.of().noOcclusion().mapColor(MapColor.STONE));
	}

	@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res){
		if(!level.isClientSide && hand != InteractionHand.OFF_HAND){
			UniEntity.get(player).entity.openUI(FsmmUIKeys.UI_ATM_MAIN, new V3I(pos.getX(), pos.getY(), pos.getZ()));
        }
		return InteractionResult.SUCCESS;
    }

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd){
		sd.add(FACING);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getPlayer().getDirection().getOpposite());
	}

}
