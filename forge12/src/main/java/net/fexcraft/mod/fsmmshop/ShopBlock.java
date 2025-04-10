package net.fexcraft.mod.fsmmshop;

import com.google.common.base.Predicate;

import net.fexcraft.lib.mc.api.registry.fBlock;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.mod.fsmm.FSMM;
import net.fexcraft.mod.fsmm.data.PlayerAccData;
import net.fexcraft.mod.fsmm.local.MoneyItem;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fBlock(modid = "fsmmshop", name = "shop", tileentity = ShopEntity.class)
public class ShopBlock extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate) EnumFacing.Plane.HORIZONTAL);
    public static ShopBlock INST;

    public ShopBlock() {
        super(Material.GLASS);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setHarvestLevel("pickaxe", 1);
        setHardness(1.0F);
        setResistance(32.0F);
        setCreativeTab(FSMM.tabFSMM);
        INST = this;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            ShopEntity tile = (ShopEntity) world.getTileEntity(pos);
            boolean sp = Static.getServer().isSinglePlayer();
            PlayerAccData data = UniEntity.get(player).getApp(PlayerAccData.class);
            if(player.isSneaking() && (tile.shop.owner == null || tile.shop.isOwner(data))){
                player.openGui(FSMMShop.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            else if(player.isSneaking() && player.capabilities.isCreativeMode && (sp || Static.isOp(player))){
                player.openGui(FSMMShop.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            else{
                player.openGui(FSMMShop.INSTANCE, 1, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new ShopEntity(world);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote){
            ShopEntity tile = (ShopEntity)world.getTileEntity(pos);
            if(tile != null) tile.dropItems();
        }
        super.breakBlock(world, pos, state);
    }

}
