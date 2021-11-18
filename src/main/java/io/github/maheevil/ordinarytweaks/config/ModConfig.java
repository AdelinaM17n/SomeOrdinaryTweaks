package io.github.maheevil.ordinarytweaks.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "ordinarytweaks")
public class ModConfig implements ConfigData {
    /**
     * the values below are default values
     */
    public boolean invisibleShield = false;
    public boolean skipResourcePackDownload = false;
    public boolean unlockAllRecipes = false;
    public boolean hideTutorialToasts = true;
    public boolean hideAdvancementToasts = false;
    public boolean hideRecipeUnlockToasts = false;

}
