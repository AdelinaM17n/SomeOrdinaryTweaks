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
  // There is two things I can do here, edit the NightVision scale which would then influence the gamma variables
  // Or Directly edit the gamma scale variable
  // The latter approach is currently used, although the former might be more stable
  // Alternative and more stable impl would be to mixin into game renderer's getNMightVisionScale method,
  // but that would mean night vision fog
  @ModifyVariable(
          method = "updateLightTexture",
          at = @At(value = "STORE"),
          index = 21, ordinal = 9
          //index = 8, ordinal = 6, indexes for night vision scale
          //print = true I am a bloody idiot
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
