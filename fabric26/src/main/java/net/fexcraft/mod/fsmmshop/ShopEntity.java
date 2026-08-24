package net.fexcraft.mod.fsmmshop;

import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ShopEntity extends BlockEntity {

    public Shop shop = new Shop();

    public ShopEntity(BlockPos pos, BlockState state){
        super(FSMMShop.SHOP_ENT, pos, state);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider prov){
        TagCW tag = TagCW.create();
        shop.write(tag);
        return tag.local();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket(){
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void saveAdditional(ValueOutput compound){
        super.saveAdditional(compound);
        shop.write(TagCW.wrap(compound));
    }

    @Override
    public void loadAdditional(ValueInput compound){
        super.loadAdditional(compound);
        shop.read(TagCW.wrap(compound));
    }

}
