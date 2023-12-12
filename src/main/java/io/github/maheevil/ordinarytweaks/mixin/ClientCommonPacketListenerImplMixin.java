package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Restriction(
        conflict = {
                @Condition("skipserverpacks")
        }
)
@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {
    @Shadow @Final protected Connection connection;

    @Inject(
            method = "handleResourcePackPush",
            at = @At(
                    target = "net/minecraft/client/multiplayer/ClientCommonPacketListenerImpl.parseResourcePackUrl (Ljava/lang/String;)Ljava/net/URL;",
                    shift = At.Shift.AFTER,
                    value = "INVOKE"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void injectAfterParseUrl$handleResourcePackPush(ClientboundResourcePackPushPacket clientboundResourcePackPushPacket, CallbackInfo ci, UUID uUID){
        if(SomeOrdinaryTweaksMod.config.skipResourcePackDownload){
            this.connection.send(new ServerboundResourcePackPacket(uUID, ServerboundResourcePackPacket.Action.ACCEPTED));
            this.connection.send(new ServerboundResourcePackPacket(uUID, ServerboundResourcePackPacket.Action.DOWNLOADED));
            this.connection.send(new ServerboundResourcePackPacket(uUID, ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
        }
    }
}
