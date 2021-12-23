package io.github.maheevil.ordinarytweaks;

import io.github.maheevil.ordinarytweaks.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class SomeOrdinaryTweaksMod implements ClientModInitializer {
	public static ModConfig config;
	@Override
	public void onInitializeClient() {
		System.out.println("Loading Some-Ordinary-Tweaks config files");
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}
}
