package io.github.maheevil.ordinarytweaks.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ordinarytweaks")
public class ModConfig implements ConfigData {
    /**
     * the values below are default values
     */
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category("Render/Gui Tweaks")
    public boolean invisibleShield = false;
    @ConfigEntry.Category("Render/Gui Tweaks")
    public boolean invisibleTotem = false;
    @ConfigEntry.Category("Render/Gui Tweaks")
    public boolean betterHorseHUD = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category("Toast Control")
    public boolean hideTutorialToasts = true;
    @ConfigEntry.Category("Toast Control")
    public boolean hideAdvancementToasts = false;
    @ConfigEntry.Category("Toast Control")
    public boolean hideRecipeUnlockToasts = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Category("General Tweaks")
    public boolean deathCordsClipBoardButton = true;
    @ConfigEntry.Category("General Tweaks")
    public boolean disablePortalGUIClosing = false;
    @ConfigEntry.Category("General Tweaks")
    public boolean skipResourcePackDownload = false;


}
