package co.runed.multicharacter.api;

import co.runed.multicharacter.character.Character;
import net.minecraft.entity.player.EntityPlayer;

public interface IAddon
{
    void init();

    IClientAddon getClientAddon();

    void setClientAddon(IClientAddon clientAddon);

    /**
     * Runs before a players characters are loaded
     *
     * @param player The player
     */
    void onPreLoad(EntityPlayer player);

    /**
     * Runs as a players characters are loaded
     *
     * @param player The player
     */
    void onLoad(EntityPlayer player);

    /**
     * Runs before a players characters are saved
     *
     * @param player The player
     */
    void onPreSave(EntityPlayer player);

    /**
     * Runs as a players characters are saved
     *
     * @param player The player
     */
    void onSave(EntityPlayer player);

    /**
     * Runs as a character is created
     *
     * @param player    The player
     * @param character The character
     */
    void onCreateCharacter(EntityPlayer player, Character character);

    /**
     * Runs as a character is selected but before it has been loaded
     *
     * @param player    The player
     * @param character The character
     */
    void onSelectCharacter(EntityPlayer player, Character character);

    /**
     * Runs when a player has disconnected
     *
     * @param player The player
     */
    void onDisconnect(EntityPlayer player);

    /**
     * Runs when a player changes dimension
     * Broken in Magma as of July 8th 2020
     *
     * @param player The player
     */
    void onChangeDimension(EntityPlayer player, int fromDim, int toDim);

    /**
     * Runs when a player respawns
     *
     * @param player The player
     */
    void onRespawn(EntityPlayer player);
}
