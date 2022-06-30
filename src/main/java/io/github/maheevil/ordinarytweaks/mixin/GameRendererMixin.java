package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
            method = "getDarkenWorldAmount",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void ordinarytweaks$getDarkenWorldAmount(float partialTicks, CallbackInfoReturnable<Float> cir){
        if(SomeOrdinaryTweaksMod.config.fullBright) {
            cir.setReturnValue(0F);
            cir.cancel();
        }
    }
}
