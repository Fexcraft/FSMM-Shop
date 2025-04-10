package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public ShopBlock(){
        super(Properties.of().noOcclusion().explosionResistance(64).strength(2));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return FSMMShop.SHOP_ENT.get().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState){
        return RenderShape.INVISIBLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res){
        if(!level.isClientSide){
            ShopEntity tile = (ShopEntity)level.getBlockEntity(pos);
            boolean sp = WrapperHolder.isSinglePlayer();
            EntityW ply = UniEntity.getEntity(player);
            PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
            if(player.isShiftKeyDown() && (tile.shop.owner == null || tile.shop.isOwner(data))){
                ply.openUI(FSUI.SHOP_EDITOR, pos.getX(), pos.getY(), pos.getZ());
            }
            else if(player.isShiftKeyDown() && player.isCreative() && (sp || WrapperHolder.isOp(ply))){
                ply.openUI(FSUI.SHOP_EDITOR, pos.getX(), pos.getY(), pos.getZ());
            }
            else{
                ply.openUI(FSUI.SHOP_VIEWER, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd){
        sd.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getPlayer().getDirection().getOpposite());
    }

}

