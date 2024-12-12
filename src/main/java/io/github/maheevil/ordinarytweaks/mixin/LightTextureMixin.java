package io.github.maheevil.ordinarytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

  @ModifyReturnValue(
          method = "getDarknessGamma", at = @At("RETURN"))
  public float darknessInject(float original){
      return !SomeOrdinaryTweaksMod.config.fullBright ? original : 0.0F;
  }
}
