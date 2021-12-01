package io.github.maheevil.ordinarytweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class BlockEntityWithoutLevelRendererMixin {

    @Inject(
            method = "renderByItem",
            at = @At(
                    target = "net/minecraft/world/item/BlockItem.getBlockEntityData(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/nbt/CompoundTag;",
                    shift = At.Shift.BEFORE,
                    value = "INVOKE"
            ),
            cancellable = true
    )
    public void renderByItemMixin(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci){
        if(SomeOrdinaryTweaksMod.config.invisibleShield){
            assert Minecraft.getInstance().player != null;
            Minecraft minecraft = Minecraft.getInstance();
            ItemStack mainHandItemStack = minecraft.player.getMainHandItem();
            ItemTransforms.TransformType mojangMadeMeDoThis = minecraft.player.getMainArm() == HumanoidArm.RIGHT
                    ? ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND : ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;

            if(transformType == mojangMadeMeDoThis && mainHandItemStack != null
                    && !(mainHandItemStack.getItem() instanceof AxeItem || mainHandItemStack.getItem() instanceof SwordItem
                    || mainHandItemStack.getItem() instanceof ProjectileWeaponItem || mainHandItemStack.getItem() instanceof TridentItem)
            )
                ci.cancel();
        }
    }
}
