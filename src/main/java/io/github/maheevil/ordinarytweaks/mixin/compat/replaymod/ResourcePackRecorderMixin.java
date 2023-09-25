package io.github.maheevil.ordinarytweaks.mixin.compat.replaymod;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.Connection;
//import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
//import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(
        require = {@Condition("replaymod")}
)
@Pseudo
@Mixin(targets = "com/replaymod/recording/packet/ResourcePackRecorder")
public abstract class ResourcePackRecorderMixin {
    /*@Shadow public abstract ServerboundResourcePackPacket makeStatusPacket(String hash, ServerboundResourcePackPacket.Action action);

    @Inject(
            method = "handleResourcePack",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    public void onResourcePackSend(Connection netManager, ClientboundResourcePackPacket packet, CallbackInfoReturnable<ClientboundResourcePackPacket> ci) {
        if(SomeOrdinaryTweaksMod.config.skipResourcePackDownload) {
            String hash = packet.getHash();
            netManager.send(this.makeStatusPacket(hash, ServerboundResourcePackPacket.Action.ACCEPTED));
            netManager.send(this.makeStatusPacket(hash, ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
            ci.cancel();
        }
    }*/
}
