package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastComponent.class)
@Environment(EnvType.CLIENT)
public abstract class ToastComponentMixin {
    @Inject(
            method = "addToast",
            at = @At("HEAD"),
            cancellable = true
    )
    public void inject_addToast(Toast toast, CallbackInfo ci){
        if(toast instanceof AdvancementToast && SomeOrdinaryTweaksMod.config.hideAdvancementToasts)
            ci.cancel();
        else if(toast instanceof RecipeToast && SomeOrdinaryTweaksMod.config.hideRecipeUnlockToasts)
            ci.cancel();
        else if(toast instanceof TutorialToast && SomeOrdinaryTweaksMod.config.hideTutorialToasts)
            ci.cancel();
    }
}
