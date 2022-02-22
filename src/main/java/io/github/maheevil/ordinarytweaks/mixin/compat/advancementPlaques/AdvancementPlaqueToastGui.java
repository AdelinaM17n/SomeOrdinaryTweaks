package io.github.maheevil.ordinarytweaks.mixin.compat.advancementPlaques;

import com.anthonyhilyard.advancementplaques.AdvancementPlaquesToastGui;
import net.minecraft.client.gui.components.toasts.Toast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(AdvancementPlaquesToastGui.class)
public class AdvancementPlaqueToastGui {
    @Inject(
            method = "addToast",
            at = @At("HEAD"),
            cancellable = true
    )
    public void inJectAtHeadAddToast(Toast displayInfo, CallbackInfo ci){
        ci.cancel();
    }
}
