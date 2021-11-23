package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Redirect(
            method = "handleNetherPortalClient",
            at = @At(value = "FIELD", target = "net/minecraft/client/Minecraft.screen:Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0)
    )
    public Screen redirect_isPauseScreen(Minecraft instance) {
        return SomeOrdinaryTweaksMod.config.disablePortalGUIClosing ? null : instance.screen;
    }
}
