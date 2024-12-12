package io.github.maheevil.ordinarytweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.maheevil.ordinarytweaks.SomeOrdinaryTweaksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(BlockEntityWithoutLevelRenderer.class)
@Mixin(ItemRenderer.class)
public abstract class BlockEntityWithoutLevelRendererMixin {

    @Inject(
            method = "renderItem",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void renderByItemMixin(
            ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, int[] is, BakedModel bakedModel, RenderType renderType, ItemStackRenderState.FoilType foilType, CallbackInfo ci
    ){
        /*
         * ItemDisplayContext.getId()'s first person ids (in decimal) are 3 (left) and 4 (right)
         * The player's main arm's ID is either 0 (left) or 1 (right)
         *
         * So when ItemDisplayContext's ID's remainder is taken after dividing by 2, its left and right should
         * mismatch with the main arm's left and right arms
         * If they are equal that means it's rendering the offhand item, And that is cancelled if the item matches
         */
        if(SomeOrdinaryTweaksMod.config.invisibleShield){
            assert Minecraft.getInstance().player != null;
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            ItemStack mainHandItemStack = localPlayer.getMainHandItem();

            if(itemDisplayContext.firstPerson() && itemDisplayContext.getId() % 2 == localPlayer.getMainArm().getId()){
                if(!(mainHandItemStack.getItem() instanceof AxeItem
                        || mainHandItemStack.getItem() instanceof SwordItem
                        || mainHandItemStack.getItem() instanceof ProjectileWeaponItem
                        || mainHandItemStack.getItem() instanceof TridentItem)
                ) ci.cancel();
            }
        }
    }
}
