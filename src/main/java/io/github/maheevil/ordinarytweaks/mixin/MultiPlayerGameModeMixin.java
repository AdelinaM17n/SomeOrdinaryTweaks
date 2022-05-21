package io.github.maheevil.ordinarytweaks.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow public abstract InteractionResult useItem(Player player, InteractionHand interactionHand);

    /*@Inject(
            method = "performUseItemOn",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/multiplayer/MultiPlayerGameMode.minecraft:Lnet/minecraft/client/Minecraft;",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            cancellable = true
    )
    public void useItemon(LocalPlayer player, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir){
        Item itemInHand = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem().getItem() : player.getOffhandItem().getItem();
        boolean isValidItem = itemInHand instanceof BlockItem && itemInHand.isEdible();
        if(SomeOrdinaryTweaksMod.config.doNotPlantEdiblesIfHungry
                && player.getFoodData().needsFood()
                && isValidItem)
        {
            cir.setReturnValue(this.useItem(player,hand));
            cir.cancel();
        }
    }*/
    @ModifyVariable(
            method = "performUseItemOn",
            at = @At("STORE"),
            index = 7,
            ordinal = 1
    )
    private boolean bruh(boolean value){
        return true;
    }
}
