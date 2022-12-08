package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
  @ModifyVariable(
          method = "updateLightTexture",
          at = @At(value = "STORE"),
          index = 8,
          ordinal = 6
  )
  public float br(float value){
      return SomeOrdinaryTweaksMod.config.fullBright ? 100 : value;
  }

  @Redirect(
          method = "getDarknessGamma",
          at = @At(
                  value = "INVOKE",
                  target = "net/minecraft/client/player/LocalPlayer.hasEffect (Lnet/minecraft/world/effect/MobEffect;)Z"
          )
  )
  public boolean darknessInject(LocalPlayer instance, MobEffect mobEffect){
      return !SomeOrdinaryTweaksMod.config.fullBright && instance.hasEffect(mobEffect);
  }
}
