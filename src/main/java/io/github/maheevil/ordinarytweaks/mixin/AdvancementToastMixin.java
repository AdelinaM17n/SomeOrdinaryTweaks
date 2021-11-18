package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AdvancementToast.class)
@Environment(EnvType.CLIENT)
public class AdvancementToastMixin {
    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "net/minecraft/advancements/Advancement.getDisplay()Lnet/minecraft/advancements/DisplayInfo;",ordinal = 0)
    )
    public DisplayInfo redirect_render(Advancement instance){
        if(SomeOrdinaryTweaksMod.config.hideAdvancementToasts)
            return null;
        return instance.getDisplay();
    }
}
