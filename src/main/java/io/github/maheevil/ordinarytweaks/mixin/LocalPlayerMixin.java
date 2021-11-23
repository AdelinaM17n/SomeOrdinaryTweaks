package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Redirect(
            method = "handleNetherPortalClient",
            at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/Screen.isPauseScreen()Z", ordinal = 0)
    )
    public boolean redirect_isPauseScreen(Screen instance){
        if(SomeOrdinaryTweaksMod.config.disablePortalGUIClosing)
            return true;
        return instance.isPauseScreen();
    }
}
