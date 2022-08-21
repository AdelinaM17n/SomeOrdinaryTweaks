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
                BlockState blockState = isHittingYAxis
                        ? level.getBlockState(blockHitResult.getBlockPos())
                        : level.getBlockState(blockHitResult.getBlockPos().relative(direction));

                if( (blockState.getBlock() instanceof SlabBlock && blockState.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM))
                        || (isHittingYAxis && level.getBlockState(blockHitResult.getBlockPos().relative(direction)).getBlock() instanceof SlabBlock))
                {
                        mutableObject.setValue(InteractionResult.CONSUME);
                        cir.setReturnValue(new ServerboundUseItemPacket(interactionHand,i));
                        cir.cancel();
                }
            }

            if(SomeOrdinaryTweaksMod.config.doNotPlantEdiblesIfHungry
                    && localPlayer.getFoodData().needsFood()
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
