package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DisplayRenderer.TextDisplayRenderer.class)
public class TextDisplayRendererMixin {
    @Redirect(
            method = "renderInner(Lnet/minecraft/world/entity/Display$TextDisplay;Lnet/minecraft/world/entity/Display$TextDisplay$TextRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/Options.getBackgroundOpacity (F)F")
    )
    private float redirectBackgroundOpacity$ordinarytweaks(Options instance, float opacity){
        return switch(SomeOrdinaryTweaksMod.config.opacitySettings.textDisplayOpacity){
            case VANILLA -> instance.getBackgroundOpacity(opacity);
            case _0_PERCENT -> 0.0F;
            case _25_PERCENT -> 0.25F;
            case _50_PERCENT -> 0.50F;
            case _75_PERCENT -> 0.75F;
            case _100_PERCENT -> 1.0F;
        };
    }
}
