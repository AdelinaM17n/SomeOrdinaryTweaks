package io.github.maheevil.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BlockEntityWithoutLevelRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class BlockEntityWithoutLevelRendererMixin {
    @Inject(
            method = "renderByItem",
            at = @At(
                    target = "net/minecraft/world/item/ItemStack.getTagElement(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;",
                    shift = At.Shift.BEFORE,
                    value = "INVOKE"
            ),
            cancellable = true
    )
    public void rendermixin(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci){
        assert Minecraft.getInstance().player != null;
        ItemStack MainHandItemStack = Minecraft.getInstance().player.getMainHandItem();

        if(
                transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND && MainHandItemStack != null
                && !(MainHandItemStack.getItem() instanceof AxeItem || MainHandItemStack.getItem() instanceof SwordItem
                || MainHandItemStack.getItem() instanceof ProjectileWeaponItem || MainHandItemStack.getItem() instanceof TridentItem)
        )
            ci.cancel();

    }
}
