package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URL;

@Restriction(
        conflict = {
                @Condition("skipserverpacks")
        }
)
@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {
    @Shadow protected abstract void send(ServerboundResourcePackPacket.Action action);

    @Inject(
            method = "handleResourcePack",
            at = @At(value = "HEAD"),
            cancellable=true
    )
    public void onResourcePackSend(ClientboundResourcePackPacket par1, CallbackInfo ci) {
        if(SomeOrdinaryTweaksMod.config.skipResourcePackDownload) {
            this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
            this.send(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED);
            ci.cancel();
        }
    }

    @Inject(
            method = "showServerPackPrompt",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void onShowPrompt(URL uRL, String string, boolean bl, Component component, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.skipResourcePackDownload) {
            this.send(ServerboundResourcePackPacket.Action.ACCEPTED);
            this.send(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED);
            ci.cancel();
        }
    }
}
