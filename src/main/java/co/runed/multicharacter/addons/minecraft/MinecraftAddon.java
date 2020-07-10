package co.runed.multicharacter.addons.minecraft;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.api.Addon;
import co.runed.multicharacter.api.MultiCharacterAPI;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.character.CharacterManager;
import co.runed.multicharacter.util.NbtUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MinecraftAddon extends Addon
{
    public static final String PLAYER_DATA_NBT_KEY = "PlayerData";

    @Override
    public void init()
    {

    }

    @Override
    public void onPreLoad(EntityPlayer player)
    {

    }

    @Override
    public void onLoad(EntityPlayer player)
    {
        CharacterManager characterManager = MultiCharacterMod.getCharacterManager();

        File characterDirectory = MultiCharacterAPI.getCharacterDirectory(player);
        File charactersFilePath = new File(characterDirectory, "characters.dat");

        if (!charactersFilePath.exists()) return;

        try
        {
            // LOAD characters.dat AS NBT
            NBTTagCompound charactersNbt = CompressedStreamTools.readCompressed(new FileInputStream(charactersFilePath));

            // LOAD NBT INTO CHARACTER MANAGER
            characterManager.deserializeCharacters(charactersNbt);

            Character activeCharacter = characterManager.getActiveCharacter(player);

            if (activeCharacter != null)
            {
                // GET PLAYER.DAT FROM CHARACTER NBT
                NBTTagCompound nbt = activeCharacter.getNbt();
                NBTTagCompound playerData = nbt.hasKey(PLAYER_DATA_NBT_KEY) ? nbt.getCompoundTag(PLAYER_DATA_NBT_KEY) : new NBTTagCompound();

                // IF SOMEHOW RUNNING ON CLIENT RETURN
                if (!(player.world instanceof WorldServer)) return;

                ((WorldServer) player.world).addScheduledTask(() ->
                {
                    // LOAD VARIABLES BEFORE THEY ARE REMOVED FROM PLAYER DATA
                    int prevDimension = player.dimension;
                    int dimension = playerData.hasKey("Dimension") ? playerData.getInteger("Dimension") : prevDimension;
                    int gameMode = playerData.hasKey("playerGameType") ? playerData.getInteger("playerGameType") : 0;

                    // REMOVE MANUALLY PROCESSED TAGS TO AVOID BREAKING PLAYER.DAT LOADING LATER
                    playerData.removeTag("playerGameType");
                    playerData.removeTag("Dimension");

                    playerData.setUniqueId("UUID", player.getUniqueID());

                    // CHANGE DIMENSION
                    if (dimension != prevDimension)
                    {
                        player.changeDimension(dimension, new CharacterLoadingTeleporter());
                    }

                    // FORCE GAMEMODE UPDATE MANUALLY
                    player.setGameType(GameType.getByID(Math.max(gameMode, 0)));

                    // FORCE POSITION UPDATE MANUALLY
                    if (playerData.hasKey("Pos"))
                    {
                        NBTTagList posList = playerData.getTagList("Pos", 6);
                        double posX = posList.getDoubleAt(0);
                        double posY = posList.getDoubleAt(1);
                        double posZ = posList.getDoubleAt(2);
                        player.setPositionAndUpdate(posX, posY, posZ);
                    }

                    // LOAD INFO FROM NBT
                    player.readFromNBT(playerData);

                    // UPDATE PLAYER IN WORLD
                    player.world.updateEntityWithOptionalForce(player, true);
                });
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreSave(EntityPlayer player)
    {
    }

    @Override
    public void onSave(EntityPlayer player)
    {
        CharacterManager characterManager = MultiCharacterMod.getCharacterManager();
        File characterDirectory = MultiCharacterAPI.getCharacterDirectory(player);

        if (characterManager.getCharacters(player).size() <= 0) return;

        Character activeCharacter = characterManager.getActiveCharacter(player);

        if (activeCharacter != null)
        {
            // WRITE PLAYER.DAT NBT TO CHARACTER NBT
            NBTTagCompound nbt = activeCharacter.getNbt();
            nbt.setTag(MinecraftAddon.PLAYER_DATA_NBT_KEY, player.writeToNBT(new NBTTagCompound()));

            activeCharacter.setNbt(nbt);
        }

        File charFile = new File(characterDirectory, "characters.dat");

        // BACKUP OLD CHARACTERS.DAT BEFORE SAVING
        if (charFile.exists())
        {
            charFile.renameTo(new File(characterDirectory, "characters_old.dat"));
        }

        // WRITE LIST OF CHARACTERS AS characters/characters.dat
        NbtUtil.writeFile(characterManager.serializeCharacters(player), characterDirectory, "characters");
    }

    @Override
    public void onDisconnect(EntityPlayer player)
    {
        // IF NO CHARACTERS DON'T DELETE PLAYER.DAT
        if (MultiCharacterMod.getCharacterManager().getCharacters(player).size() <= 0) return;

        // DELETE PLAYER.DAT
        File playerDat = this.getExistingPlayerDatFile(player);

        if (playerDat.exists())
        {
            playerDat.delete();
        }
    }

    @Override
    public void onCreateCharacter(EntityPlayer player, Character character)
    {
        NBTTagCompound nbt = character.getNbt();

        // CREATE NEW PLAYER AND SERIALIZE TO NBT TO GET BASE VALUES FOR NEW CHARACTER
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        Entity entity = new EntityPlayerMP(server, server.getWorld(0), player.getGameProfile(), new PlayerInteractionManager(player.world));
        NBTTagCompound playerNbt = entity.writeToNBT(new NBTTagCompound());

        playerNbt.setUniqueId("UUID", player.getUniqueID());

        // IF THIS IS THE FIRST CHARACTER A PLAYER CREATES, SET CHARACTER NBT TO PLAYER NBT
        if (MultiCharacterMod.getCharacterManager().getCharacters(player).size() <= 1)
        {
            File playerDat = this.getExistingPlayerDatFile(player);

            try
            {
                NBTTagCompound existingNbt = CompressedStreamTools.readCompressed(new FileInputStream(playerDat));
                int gameMode = existingNbt.hasKey("playerGameType") ? existingNbt.getInteger("playerGameType") : 0;

                if (gameMode != -1 && gameMode != 3) {
                    playerNbt = existingNbt;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            playerNbt.setInteger("playerGameType", FMLCommonHandler.instance().getMinecraftServerInstance().getGameType().getID());
        }

        nbt.setTag(PLAYER_DATA_NBT_KEY, playerNbt);

        character.setNbt(nbt);
    }

    @Override
    public void onSelectCharacter(EntityPlayer player, Character character)
    {

    }

    @Override
    public void onChangeDimension(EntityPlayer player, int fromDim, int toDim)
    {

    }

    @Override
    public void onRespawn(EntityPlayer player)
    {

    }

    private File getExistingPlayerDatFile(EntityPlayer player)
    {
        File playerDirectory = new File(player.world.getSaveHandler().getWorldDirectory(), "playerdata");
        File playerDat = new File(playerDirectory, player.getUniqueID().toString() + ".dat");

        return playerDat;
    }
}
