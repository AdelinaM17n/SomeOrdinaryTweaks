package io.github.maheevil.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

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
        if(transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND && this.blockEntityRenderDispatcher.level.players().get(0).getMainHandItem() != null &&!this.blockEntityRenderDispatcher.level.players().get(0).getMainHandItem().is(Items.STONE_SWORD)) {
            ci.cancel();
        }
    }
}
