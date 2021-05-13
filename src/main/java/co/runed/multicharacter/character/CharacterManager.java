package co.runed.multicharacter.character;

import co.runed.multicharacter.api.IAddon;
import co.runed.multicharacter.api.MultiCharacterAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.*;

public class CharacterManager
{
    Map<UUID, List<UUID>> playerCharacters = new HashMap<>();

    Map<UUID, UUID> activeCharacter = new HashMap<>();
    Map<UUID, Character> characters = new HashMap<>();

    /**
     * Sets character at a specific index
     * Used for character editing
     *
     * @param playerUuid    The player
     * @param index     Index
     * @param character The character
     */
    public void setCharacter(UUID playerUuid, int index, Character character)
    {
        this.playerCharacters.putIfAbsent(playerUuid, new ArrayList<>());

        this.characters.put(character.getUniqueId(), character);
        this.playerCharacters.get(playerUuid).set(index, character.getUniqueId());
    }

    public void addCharacter(UUID playerUuid, Character character)
    {
        this.playerCharacters.putIfAbsent(playerUuid, new ArrayList<>());

        this.playerCharacters.get(playerUuid).add(character.getUniqueId());
        this.characters.put(character.getUniqueId(), character);

        EntityPlayer player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(playerUuid);

        this.onCreateCharacter(player, character);

        this.save(playerUuid);
    }

    public void removeCharacter(UUID playerUuid, Character character)
    {
        this.playerCharacters.putIfAbsent(playerUuid, new ArrayList<>());

        if (this.getActiveCharacter(playerUuid) == character)
        {
            this.setActiveCharacter(playerUuid, null);
        }

        this.playerCharacters.get(playerUuid).removeIf((id) -> id.equals(character.getUniqueId()));
        this.characters.remove(character.getUniqueId());

        this.save(playerUuid);
    }

    /***
     *
     * @param playerUuid
     * @return
     */
    public List<Character> getCharacters(UUID playerUuid)
    {
        this.playerCharacters.putIfAbsent(playerUuid, new ArrayList<>());

        List<Character> characters = new ArrayList<>();

        for (UUID uuid : this.playerCharacters.get(playerUuid))
        {
            if (this.characters.containsKey(uuid))
            {
                characters.add(this.characters.get(uuid));
            }
        }

        return Collections.unmodifiableList(characters);//this.playerCharacters.get(player);
    }

    public Character getActiveCharacter(UUID playerUuid)
    {
        if (!this.activeCharacter.containsKey(playerUuid)) return null;

        UUID characterUUID = this.activeCharacter.get(playerUuid);

        return this.characters.get(characterUUID);
    }

    public void setActiveCharacter(UUID playerUuid, int index)
    {
        if (index >= this.getCharacters(playerUuid).size()) return;

        this.setActiveCharacter(playerUuid, this.getCharacters(playerUuid).get(index));
    }

    public void setActiveCharacter(UUID playerUuid, Character character)
    {
        if (!this.getCharacters(playerUuid).contains(character)) return;
        Character active = this.getActiveCharacter(playerUuid);
        if (active != null && active.equals(character)) return;

        this.save(playerUuid);

        this.activeCharacter.put(playerUuid, character.getUniqueId());

        EntityPlayer player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(playerUuid);

        this.onSelectCharacter(player, character);

        this.load(playerUuid);

        this.onPostSelectCharacter(player, character);
    }

    public NBTTagCompound serializeCharacters(UUID player)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList nbtList = new NBTTagList();

        nbt.setUniqueId("uuid", player);

        for (Character character : this.getCharacters(player))
        {
            nbtList.appendTag(character.serializeNBT());
        }

        nbt.setTag("characters", nbtList);

        return nbt;
    }

    public void deserializeCharacters(NBTTagCompound nbt)
    {
        UUID uuid = nbt.getUniqueId("uuid");
        NBTTagList nbtList = nbt.getTagList("characters", Constants.NBT.TAG_COMPOUND);

        List<UUID> uuids = new ArrayList<>();

        for (int i = 0; i < nbtList.tagCount(); i++)
        {
            Character character = new Character();

            NBTTagCompound charNbt = nbtList.getCompoundTagAt(i);
            character.deserializeNBT(charNbt);

            uuids.add(character.getUniqueId());
            this.characters.put(character.getUniqueId(), character);
        }

        this.playerCharacters.put(uuid, uuids);
    }

    public void save(UUID playerUUID)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        EntityPlayer player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(playerUUID);

        for (IAddon loader : integrations)
        {
            loader.onPreSave(player);
        }

        for (IAddon loader : integrations)
        {
            loader.onSave(player);
        }
    }

    public void load(UUID playerUUID)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        EntityPlayer player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(playerUUID);

        for (IAddon loader : integrations)
        {
            loader.onPreLoad(player);
        }

        for (IAddon loader : integrations)
        {
            loader.onLoad(player);
        }
    }

    public void onDisconnect(EntityPlayer player)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        this.save(player.getUniqueID());

        for (IAddon loader : integrations)
        {
            loader.onDisconnect(player);
        }

        for (Character c : this.getCharacters(player.getUniqueID()))
        {
            this.characters.remove(c.getUniqueId());
        }

        this.activeCharacter.remove(player.getUniqueID());
        this.playerCharacters.remove(player.getUniqueID());
    }

    public void onSelectCharacter(EntityPlayer player, Character character)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onSelectCharacter(player, character);
        }
    }

    public void onPostSelectCharacter(EntityPlayer player, Character character)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onPostSelectCharacter(player, character);
        }
    }

    public void onCreateCharacter(EntityPlayer player, Character character)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onCreateCharacter(player, character);
        }
    }

    public void onChangeDimension(EntityPlayer player, int fromDim, int toDim)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onChangeDimension(player, fromDim, toDim);
        }
    }

    public void onRespawn(EntityPlayer player)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onRespawn(player);
        }
    }
}
