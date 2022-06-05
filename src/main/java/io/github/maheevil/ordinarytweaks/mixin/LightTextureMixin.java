package io.github.maheevil.ordinarytweaks.mixin;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
  @ModifyVariable(
          method = "updateLightTexture",
          at = @At(value = "STORE"),
          index = 21,
          ordinal = 14
          //print = true
  )
  public float br(float value){
      return 100;
  }
}
