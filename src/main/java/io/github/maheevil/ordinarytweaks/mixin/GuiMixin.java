package io.github.maheevil.ordinarytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
@Environment(EnvType.CLIENT)
public abstract class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract void renderExperienceBar(GuiGraphics guiGraphics, int i);

    @Shadow protected abstract int getVehicleMaxHearts(@Nullable LivingEntity livingEntity);

    @Shadow @Nullable protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow public abstract void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker);

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(
            method = "renderVehicleHealth",
            at = @At("STORE"),
            index = 5,
            ordinal = 2
    )
    private int modify_int_k(int i, GuiGraphics guiGraphics){
        return (SomeOrdinaryTweaksMod.config.betterHorseHUD && this.minecraft.gameMode.getPlayerMode() != GameType.CREATIVE) ? guiGraphics.guiHeight() - 49 : i;
    }


    @ModifyExpressionValue(
            method = "renderPlayerHealth",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I")
    )
    private int modify_int_y(int in){
        return SomeOrdinaryTweaksMod.config.betterHorseHUD ? 0 : in;
    }

    @Inject(
            method = "renderHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderJumpMeter(Lnet/minecraft/world/entity/PlayerRideableJumping;Lnet/minecraft/client/gui/GuiGraphics;I)V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )private void renderXpPostJumpMeter(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci, int i, PlayerRideableJumping playerRideableJumping){
        if(SomeOrdinaryTweaksMod.config.betterHorseHUD && this.minecraft.gameMode.hasExperience() && !this.minecraft.options.keyJump.isDown()){
            this.renderExperienceBar(guiGraphics,i);
        }
    }

    @WrapWithCondition(
            method = "renderHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderJumpMeter(Lnet/minecraft/world/entity/PlayerRideableJumping;Lnet/minecraft/client/gui/GuiGraphics;I)V"
            )
    )
    private boolean wrapRenderJumpMeter(Gui instance, PlayerRideableJumping playerRideableJumping, GuiGraphics guiGraphics, int i){
        return !SomeOrdinaryTweaksMod.config.betterHorseHUD || !this.minecraft.gameMode.hasExperience() || this.minecraft.options.keyJump.isDown();
    }

    @ModifyArg(
            method = "renderPlayerHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderAirBubbles(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;III)V"
            ),
            index = 2
    )private int giveCorrectIValToAirBubbles(int i){
        if(SomeOrdinaryTweaksMod.config.betterHorseHUD &&  i != 0)
            return this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth());
        else
            return i;
    }

    @ModifyArg(
            method = "renderPlayerHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderAirBubbles(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;III)V"
            ),
            index = 3
    )private int giveCorrectRValToAirBubbles(int i){
        if(SomeOrdinaryTweaksMod.config.betterHorseHUD &&  i != 0)
            return i - 10;
        else
            return i;
    }

    @ModifyExpressionValue(
            method = "renderExperienceLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isExperienceBarVisible()Z")
    )
    private boolean makeXpLevelVisible(boolean original){
        if(SomeOrdinaryTweaksMod.config.betterHorseHUD && this.minecraft.gameMode.hasExperience()) {
            return true;
        }
        return original;
    }

    //@SuppressWarnings("all") // MCDEV STOP TRYING TO GASLIGHT ME
    @ModifyVariable(
            method = "renderItemHotbar",
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
    private void inject_scoreboard$ordinarytweaks(GuiGraphics guiGraphics, Objective objective, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.disableScoreboard) ci.cancel();
    }
}
