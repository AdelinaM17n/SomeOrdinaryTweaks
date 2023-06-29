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
import org.spongepowered.asm.mixin.Unique;
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
    public void beforeInteractionInject$ordinarytweaks(
            MutableObject<InteractionResult> mutableObject,
            LocalPlayer localPlayer,
            InteractionHand interactionHand,
            BlockHitResult blockHitResult,
            int i,
            CallbackInfoReturnable<Packet<?>> cir
    ){
        Item itemInHand = localPlayer.getItemInHand(interactionHand).getItem();

        if(itemInHand instanceof BlockItem blockItem){
            Level level = localPlayer.clientLevel;
            BlockState theBlockGettingHit = level.getBlockState(blockHitResult.getBlockPos());

            if(SomeOrdinaryTweaksMod.config.noDoubleSlabPlacement && blockItem.getBlock() instanceof SlabBlock){
                if(
                        !theBlockGettingHit.use(level,localPlayer, interactionHand, blockHitResult).consumesAction()
                        || localPlayer.isSecondaryUseActive()
                ){
                    Direction direction = blockHitResult.getDirection();
                    boolean isHittingYAxis = direction.getAxis() == Direction.Axis.Y;
                    BlockState blockState;

                    if(isHittingYAxis){
                        blockState = theBlockGettingHit;
                        BlockState relativeBlockState = level.getBlockState(blockHitResult.getBlockPos().relative(direction,1));
                        handleHittingYAxis(
                                mutableObject,
                                interactionHand,
                                i, cir,
                                blockState,
                                relativeBlockState,
                                direction.equals(Direction.UP) ? SlabType.BOTTOM : SlabType.TOP,
                                direction.equals(Direction.UP) ? SlabType.TOP : SlabType.BOTTOM
                        );
                    }else {
                        blockState = level.getBlockState(blockHitResult.getBlockPos().relative(direction));
                        // if the block relative to the direction of the targeted block side is a slab block, it would form a double slab block
                        // this will not prevent none double-slab-block forming placements
                        if(blockState.getBlock() instanceof SlabBlock){
                            mutableObject.setValue(InteractionResult.FAIL);
                            cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                            cir.cancel();
                        }
                    }
                }
            }

            if(SomeOrdinaryTweaksMod.config.doNotPlantEdiblesIfHungry && blockItem.isEdible()){
                if(// !(x XOR y)
                        theBlockGettingHit.use(level, localPlayer, interactionHand, blockHitResult).consumesAction()
                            ==
                        localPlayer.isSecondaryUseActive()
                ){
                    mutableObject.setValue(useItem(localPlayer, interactionHand));
                    cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
                    cir.cancel();
                }

            }
        }
    }

    @Unique
    private void handleHittingYAxis(
            MutableObject<InteractionResult> mutableObject,
            InteractionHand interactionHand,
            int i, CallbackInfoReturnable<Packet<?>> cir,
            BlockState blockState,
            BlockState relativeBlockState,
            SlabType targetSlabTypeRequirement,
            SlabType relativeSlabSizeRequirement
    ){
        if(// if targeted slab block is a top, placing a block from bellow would form a double slab, and vice versa
                (blockState.getBlock() instanceof SlabBlock && blockState.getValue(SlabBlock.TYPE).equals(targetSlabTypeRequirement))
                // if the relative block is a slab block and is a bottom, clicking on the targeted bottom slab block would form a double slab, and vice versa
                || (relativeBlockState.getBlock() instanceof SlabBlock && relativeBlockState.getValue(SlabBlock.TYPE).equals(relativeSlabSizeRequirement))
        )
        {
            mutableObject.setValue(InteractionResult.FAIL);
            cir.setReturnValue(new ServerboundUseItemPacket(interactionHand, i));
            cir.cancel();
        }
    }
}
