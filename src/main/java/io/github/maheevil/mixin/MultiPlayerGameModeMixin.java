package io.github.maheevil.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
@Environment(EnvType.CLIENT)
public class MultiPlayerGameModeMixin {
    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    public void $mixin_attack(Player player, Entity entity, CallbackInfo ci){
        if(entity instanceof TamableAnimal && ((TamableAnimal)entity).isTame())
            ci.cancel();
    }
}
