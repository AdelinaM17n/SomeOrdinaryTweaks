package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(
            method = "renderNameTag",
            at = @At(value = "INVOKE", target = "net/minecraft/client/Options.getBackgroundOpacity (F)F")
    )
    private float redirectBackgroundOpacity$ordinarytweaks(Options instance, float opacity){
        return switch(SomeOrdinaryTweaksMod.config.opacitySettings.nameTagOpacity){
            case VANILLA -> instance.getBackgroundOpacity(opacity);
            case _0_PERCENT -> 0.0F;
            case _25_PERCENT -> 0.25F;
            case _50_PERCENT -> 0.50F;
            case _75_PERCENT -> 0.75F;
            case _100_PERCENT -> 1.0F;
        };
    }
}
