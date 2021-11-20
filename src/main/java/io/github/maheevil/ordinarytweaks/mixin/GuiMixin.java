package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
@Environment(EnvType.CLIENT)
public abstract class GuiMixin extends GuiComponent {

    @Shadow protected abstract int getVisibleVehicleHeartRows(int i);
    @Shadow protected abstract int getVehicleMaxHearts(LivingEntity livingEntity);
    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();
    @Shadow private int screenHeight;
    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(
            method = "renderVehicleHealth",
            at = @At("STORE"),
            index = 5,
            ordinal = 2
    )
    private int modify_int_k(int i){
        return (SomeOrdinaryTweaksMod.config.betterHorseHUD && this.minecraft.gameMode.getPlayerMode() != GameType.CREATIVE) ? this.screenHeight - 49 :i;
    }

    @ModifyVariable(
            method = "renderPlayerHealth",
            at = @At("STORE"),
            index = 22,
            ordinal = 13
    )
    private int modify_int_y(int in){
        return SomeOrdinaryTweaksMod.config.betterHorseHUD ? 0 : in;
    }

    @ModifyArg(
            method = "renderPlayerHealth",
            at = @At(value ="INVOKE", target = "net/minecraft/client/gui/Gui.getVisibleVehicleHeartRows(I)I", ordinal = 0),
            index = 0
    )
    private int modify_methodArg_vehicleMaxHearts(int original){
        return SomeOrdinaryTweaksMod.config.betterHorseHUD
                ? this.getVisibleVehicleHeartRows(this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth())) : original;
    }

}
