package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Redirect(
            method = "handlePortalTransitionEffect",
            at = @At(value = "FIELD", target = "net/minecraft/client/Minecraft.screen:Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0, opcode = Opcodes.GETFIELD)
    )
    public Screen redirect_isPauseScreen(Minecraft instance) {
        return SomeOrdinaryTweaksMod.config.disablePortalGUIClosing ? null : instance.screen;
    }
}
