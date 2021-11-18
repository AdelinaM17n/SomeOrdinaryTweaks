package io.github.maheevil.ordinarytweaks;

import io.github.maheevil.ordinarytweaks.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class SomeOrdinaryTweaksMod implements ModInitializer {
	public static ModConfig config;
	@Override
	public void onInitialize() {
		System.out.println("Haai");
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}
}
