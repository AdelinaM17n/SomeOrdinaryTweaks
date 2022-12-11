package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow public abstract InteractionResult useItem(Player player, InteractionHand interactionHand);

    @Inject(
            method = "method_41933(Lorg/apache/commons/lang3/mutable/MutableObject;Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;I)Lnet/minecraft/network/protocol/Packet;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injectTest(MutableObject<InteractionResult> mutableObject, LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult, int i, CallbackInfoReturnable<Packet<?>> cir){
        Item itemInHand = localPlayer.getItemInHand(interactionHand).getItem();

        if(itemInHand instanceof BlockItem blockItem){

            if(SomeOrdinaryTweaksMod.config.noDoubleSlabPlacement && blockItem.getBlock() instanceof SlabBlock){
                Level level = localPlayer.clientLevel;
                Direction direction = blockHitResult.getDirection();
                boolean isHittingYAxis = direction.getAxis() == Direction.Axis.Y;
                BlockState blockState;

                if(isHittingYAxis){
                    blockState = level.getBlockState(blockHitResult.getBlockPos());
                    BlockState relativeBlockState = level.getBlockState(blockHitResult.getBlockPos().relative(direction,1));

                    if(blockState.getBlock() instanceof SlabBlock){
                        SlabType bsSlabType = blockState.getValue(SlabBlock.TYPE);
                        if(direction.equals(Direction.UP)){
                            if(
                                    // if targeted slab block is a bottom, placing a block from up would form a double slab
                                    bsSlabType.equals(SlabType.BOTTOM)
                                    // if the relative block is a slab block and is a top, clicking on the targeted top slab block would form a double slab
                                    || (relativeBlockState.getBlock() instanceof SlabBlock && relativeBlockState.getValue(SlabBlock.TYPE).equals(SlabType.TOP))
                            )
                            {
                                mutableObject.setValue(InteractionResult.CONSUME);
                                cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                                cir.cancel();
                            }
                        }else {
                            if(
                                    // if targeted slab block is a top, placing a block from bellow would form a double slab
                                    bsSlabType.equals(SlabType.TOP)
                                    // if the relative block is a slab block and is a bottom, clicking on the targeted bottom slab block would form a double slab
                                    || (relativeBlockState.getBlock() instanceof SlabBlock && relativeBlockState.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM))
                            )
                            {
                                mutableObject.setValue(InteractionResult.CONSUME);
                                cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                                cir.cancel();
                            }
                        }
                    }else {
                        // This feels like a war crime, I am once again doing the Y axis relative block check but this time if the targeted block is not a slab
                        if(direction.equals(Direction.UP)){
                            // referer to comments above
                            if(relativeBlockState.getBlock() instanceof SlabBlock && relativeBlockState.getValue(SlabBlock.TYPE).equals(SlabType.TOP)){
                                mutableObject.setValue(InteractionResult.CONSUME);
                                cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                                cir.cancel();
                            }
                        }else {
                            // refer to comments above
                            if(relativeBlockState.getBlock() instanceof  SlabBlock && relativeBlockState.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM)){
                                mutableObject.setValue(InteractionResult.CONSUME);
                                cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                                cir.cancel();
                            }
                        }
                    }
                }else {
                    // if the block relative to the direction of the targeted block side is a slab block, it would form a double slab block
                    // this will not prevent none double-slab-block forming placements
                    blockState = level.getBlockState(blockHitResult.getBlockPos().relative(direction));
                    if(blockState.getBlock() instanceof SlabBlock){
                        mutableObject.setValue(InteractionResult.CONSUME);
                        cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                        cir.cancel();
                    }
                }
            }

            if(SomeOrdinaryTweaksMod.config.doNotPlantEdiblesIfHungry
                    && blockItem.isEdible()
                    && !localPlayer.isSecondaryUseActive())
            {
                mutableObject.setValue(useItem(localPlayer, interactionHand));
                cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                cir.cancel();
            }
        }
    }

}
