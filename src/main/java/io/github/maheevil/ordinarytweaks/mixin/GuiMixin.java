package io.github.maheevil.ordinarytweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "net/minecraft/client/player/LocalPlayer.isRidingJumpable()Z", ordinal = 0)
    )
    private boolean redirect_isRidingJumpable_IF(LocalPlayer instance){
        return SomeOrdinaryTweaksMod.config.betterHorseHUD && this.minecraft.gameMode.getPlayerMode() != GameType.CREATIVE
                ? this.minecraft.options.keyJump.isDown() && instance.isRidingJumpable()
                : instance.isRidingJumpable();
    }

    @SuppressWarnings("all") // MCDEV STOP TRYING TO GASLIGHT ME
    @ModifyVariable(
            method = "renderHotbar",
            at = @At("STORE"),
            index =  5,
            ordinal = 0
    )
    private HumanoidArm modifyHumanoidArm(HumanoidArm value){
        return switch(SomeOrdinaryTweaksMod.config.offHandSlotLoc){
            case VANILA -> value;
            case LEFT -> HumanoidArm.LEFT;
            case RIGHT -> HumanoidArm.RIGHT;
        };
    }

    @Inject(
            method = "displayScoreboardSidebar",
            at = @At("HEAD"),
            cancellable = true
    )
    private void inject_scoreboard$ordinarytweaks(PoseStack poseStack, Objective objective, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.disableScoreboard) ci.cancel();
    }
}
