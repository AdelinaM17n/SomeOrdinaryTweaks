package io.github.maheevil.ordinarytweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(
            method = "renderItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injectAtHead_renderItem(
            LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext,
            boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource,
            int i, CallbackInfo ci
    ){
        /*
         * ItemDisplayContext.getId()'s first person ids (in decimal) are 3 (left) and 4 (right)
         * The player's main arm's ID is either 0 (left) or 1 (right)
         *
         * So when ItemDisplayContext's ID's remainder is taken after dividing by 2, its left and right should
         * mismatch with the main arm's left and right arms
         * If they are equal that means it's rendering the offhand item, And that is cancelled if the item matches
         */
        if(SomeOrdinaryTweaksMod.config.invisibleTotem){
            assert Minecraft.getInstance().player != null;
            if(itemDisplayContext.firstPerson()
                    && itemDisplayContext.getId() % 2 == Minecraft.getInstance().player.getMainArm().getId()
                    && itemStack.is(Items.TOTEM_OF_UNDYING)
            ) ci.cancel();
        }
    }
}
