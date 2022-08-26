# SomeOrdinaryTweaks Mod

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

## License

LGPL-3.0

## Planned features
Shield Indicator crosshair and hotbar icon ~~("Inspired" from the combat snapshot)~~   

## Notable Dependencies 
[cloth-config by shedaniel](https://github.com/shedaniel/cloth-config)   


## Forge port
https://github.com/MaheEvil/SomeOrdinaryTweaks-Forge

## Quilt?
I have removed the usage of mixin plugins, so fabric jar should be usable in quilt   
I originally intended to completely move to quilt-loader but due to reasons listed below I will not move myself just yet   
   
**YES, I know QSL is meant to be modular and now I have gotten word that in the future the qsl module that has the entry-point for the mod will be updated fast, so the bellow reasons are bit outdated but still relvenat**   
- **QSL does not update to snapshots**\* - I don't have much uses for QSL, but I have to use/depend on it due to the fact that the mod entry point is moved there.(This might just be for the current time, and I am only talking about the qsl module with the entrypoint)
- **It does not offer any technical advantage that I value yet** - Things like quiltflower are wonderful, but I already have the ability to use them on a fabric loom project.Mojmaps is my mappings of choice so quilt-mappings are not useful for me.
- **I just need a mod-loader that loads mixins** - I am really disappointed by the fact mod entrypoint-API is moved to QSL, making mod development on snapshots painful.
   
While I'd prefer to switch development of this mod to quilt, I currently can not do that.
