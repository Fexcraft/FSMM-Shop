package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEntity extends BlockEntity {

    public Shop shop = new Shop();

    public ShopEntity(BlockPos pos, BlockState state){
        super(FSMMShop.SHOP_ENT.get(), pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(){
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void saveAdditional(CompoundTag compound){
        super.saveAdditional(compound);
        shop.write(TagCW.wrap(compound));
    }

    @Override
    public void load(CompoundTag compound){
        super.load(compound);
        shop.read(TagCW.wrap(compound));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing){
        if(capability == ForgeCapabilities.ITEM_HANDLER){
            return LazyOptional.of(shop.inventory.cast());
        }
        return super.getCapability(capability, facing);
    }

}
