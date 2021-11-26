package io.github.maheevil.ordinarytweaks.mixin;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SplashManager.class)
public abstract class SplashManagerMixin extends SimplePreparableReloadListener<List<String>> {
    @Shadow @Final private List<String> splashes;

    @Inject(
            method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL")
    )
    protected void injectAtTail_ToAddSplash(List<String> list, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci){
        this.splashes.addAll(List.of("Trans Rights!","Non-Binary Rights!","The Minecraft bee is trans","[Generic Greeting]","Cone."));
    }
}
