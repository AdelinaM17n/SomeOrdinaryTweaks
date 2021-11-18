package io.github.maheevil.ordinarytweaks.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.maheevil.ordinarytweaks.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class SomeOrdinaryTweaksIntergration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}
