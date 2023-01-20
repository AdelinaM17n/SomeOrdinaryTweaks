package io.github.maheevil.ordinarytweaks.config;

import io.github.maheevil.ordinarytweaks.config.enums.OffHandSlotLoc;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ordinarytweaks")
public class ModConfig implements ConfigData {
    /**
     * the values below are default values
     */
    @ConfigEntry.Gui.PrefixText
    public boolean invisibleShield = false;
    public boolean invisibleTotem = false;
    public boolean betterHorseHUD = false;
    public boolean fullBright = false;
    public boolean disableScoreboard = false;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public OffHandSlotLoc offHandSlotLoc = OffHandSlotLoc.VANILA;
    @ConfigEntry.Gui.PrefixText
    public boolean hideTutorialToasts = true;
    public boolean hideAdvancementToasts = false;
    public boolean hideRecipeUnlockToasts = false;
    @ConfigEntry.Gui.PrefixText
    public boolean deathCordsClipBoardButton = true;
    public boolean sendDeathCords = false;
    public boolean disablePortalGUIClosing = false;
    public boolean doNotPlantEdiblesIfHungry = false;
    public boolean noDoubleSlabPlacement = false;
    public boolean skipResourcePackDownload = false;
    @ConfigEntry.Gui.RequiresRestart
    public boolean noSplash = false;

}
