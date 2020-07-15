# Multi Character Mod
A mod to add multiple character support to Minecraft 1.12.2!

When you join a server with this mod installed, a gui should pop up where you can select or create a character. You can open this screen again by pressing the `Change Character` key (default `'`). If you don't select a character you will be kicked from the server.

To report a bug, please make an issue on this repository or message `Noojuno#5466` on Discord.

## Notes

**This is an alpha version of the mod! Expect bugs!**

### Storage
- This mod changes how player data is saved to work on a per character basis. A players data is no longer saved in `<WORLD FOLDER>/playerdata/<UUID>.dat`, but is instead saved in `<WORLD FOLDER>/characters/<UUID>/characters.dat`. The `characters.dat` file will contain all of the data for every character a player creates.
- **MAKE SURE YOU BACKUP ANY EXISTING PLAYER DATA!** By default this mod will load your existing player data into your first character when you create it, but things may go wrong.

### Mod Integration
- This mod has built in integration with `More Player Models` to save your model data to your character rather than your player. As a result, a players model data is no longer saved in `moreplayermodels/<UUID>.dat` and is instead saved in your `characters.dat`.
- If a players model is resetting when either changing dimension or setting it while in a dimension other than the overworld, go to each dimension and run the command `/gamerule mpmAllowEntityModels true`

## Building
- Download More Player Models for 1.12.2 from [here](https://www.curseforge.com/minecraft/mc-mods/more-player-models) and rename the jar to `moreplayermodels.jar`. Put the jar in a folder called `libs` in the projects root directory.
- Follow Minecraft Forges instructions on building a mod.

## Commands
**Index starts at 1**
- `/setrole <player> <index> <role>`
    - sets a players role text in a specific slot
- `/addrole <player> <role>`
    - adds a new role to the players next available slot
- `/removerole <player> <role|index>`
    - removes a role from a player by either exact value or index
- `/opencharactergui <player>`
    - opens character gui for a player

## To-do:
- [ ] Character role system
    - A role should have a name and a list of commands to run when selected (so permissions, etc can be given)
    - The mod should also have a general list of commands to run when a role is deselected
- [ ] Custom kick message when trying to play without selecting a character
- [ ] Back up character when deleted
- [ ] Move More Player Models customisation to the character creation screen?
