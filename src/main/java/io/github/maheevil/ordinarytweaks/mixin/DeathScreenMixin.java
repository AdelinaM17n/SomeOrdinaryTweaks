package io.github.maheevil.ordinarytweaks.mixin;

import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
@SuppressWarnings("ConstantConditions")
public abstract class DeathScreenMixin extends Screen {
    @Shadow
    @Final
    private List<Button> exitButtons;
    @Unique
    private String lastDeathCord;
    protected DeathScreenMixin(Component component) {
        super(component);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "net/minecraft/client/gui/screens/Screen.render (Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
            )
    )
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.deathCordsClipBoardButton){
            assert this.minecraft != null;
            assert this.minecraft.player != null;
            String xyz = "[X: " + minecraft.player.blockPosition().getX() + "/ Y: " + minecraft.player.blockPosition().getY() + "/ Z: " + minecraft.player.blockPosition().getZ() + "]";
            guiGraphics.drawCenteredString(this.font, xyz, this.width / 2, this.height / 4 + 145, 16777215);
        }
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "FIELD",
                    shift = At.Shift.BEFORE,
                    target = "net/minecraft/client/gui/screens/DeathScreen.exitButtons : Ljava/util/List;",
                    ordinal = 2
            )
    )
    protected void init(CallbackInfo ci){
        String xyz =  this.minecraft.player.blockPosition().getX() + " / " + minecraft.player.blockPosition().getY() + " / " + minecraft.player.blockPosition().getZ();
        if(SomeOrdinaryTweaksMod.config.deathCordsClipBoardButton){
            this.exitButtons.add(
                    this.addRenderableWidget(
                            new Button.Builder(
                                    Component.literal("Copy Location To Clipboard"),
                                    button -> this.minecraft.keyboardHandler.setClipboard(xyz)
                            ).bounds(
                                    this.width / 2 - 100, this.height / 4 + 120, 200, 20
                            ).build()
                    )
            );
        }
        this.lastDeathCord = xyz;
    }

    @Inject(
            method = "method_19809(Lnet/minecraft/client/gui/components/Button;)V",
            at = @At(
                    value = "TAIL"
            )
    )
    private void handle(Button button, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.sendDeathCords)
            this.minecraft.player.sendSystemMessage(Component.literal(lastDeathCord));
    }

    @Inject(
            method = "method_47939(Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/Minecraft.setScreen (Lnet/minecraft/client/gui/screens/Screen;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void handleConfirm(boolean bl, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.sendDeathCords){
            this.minecraft.player.sendSystemMessage(Component.literal(lastDeathCord));
        }
    }
}
