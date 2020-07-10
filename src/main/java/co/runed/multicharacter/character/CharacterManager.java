package co.runed.multicharacter.character;

import co.runed.multicharacter.api.IAddon;
import co.runed.multicharacter.api.MultiCharacterAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

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
     * @param player    The player
     * @param index     Index
     * @param character The character
     */
    public void setCharacter(EntityPlayer player, int index, Character character)
    {
        this.playerCharacters.putIfAbsent(player.getUniqueID(), new ArrayList<>());

        this.characters.put(character.getUniqueId(), character);
        this.playerCharacters.get(player.getUniqueID()).set(index, character.getUniqueId());
    }

    public void addCharacter(EntityPlayer player, Character character)
    {
        this.playerCharacters.putIfAbsent(player.getUniqueID(), new ArrayList<>());

        this.playerCharacters.get(player.getUniqueID()).add(character.getUniqueId());
        this.characters.put(character.getUniqueId(), character);

        this.onCreateCharacter(player, character);

        this.save(player);
    }

    public void removeCharacter(EntityPlayer player, Character character)
    {
        this.playerCharacters.putIfAbsent(player.getUniqueID(), new ArrayList<>());

        if (this.getActiveCharacter(player) == character)
        {
            this.setActiveCharacter(player, null);
        }

        this.playerCharacters.get(player.getUniqueID()).removeIf((id) -> id.equals(character.getUniqueId()));
        this.characters.remove(character.getUniqueId());

        this.save(player);
    }

    /***
     *
     * @param player
     * @return
     */
    public List<Character> getCharacters(EntityPlayer player)
    {
        this.playerCharacters.putIfAbsent(player.getUniqueID(), new ArrayList<>());

        List<Character> characters = new ArrayList<>();

        for (UUID uuid : this.playerCharacters.get(player.getUniqueID()))
        {
            if (this.characters.containsKey(uuid))
            {
                characters.add(this.characters.get(uuid));
            }
        }

        return Collections.unmodifiableList(characters);//this.playerCharacters.get(player.getUniqueID());
    }

    public Character getActiveCharacter(EntityPlayer player)
    {
        if (!this.activeCharacter.containsKey(player.getUniqueID())) return null;

        UUID characterUUID = this.activeCharacter.get(player.getUniqueID());

        return this.characters.get(characterUUID);
    }

    public void setActiveCharacter(EntityPlayer player, int index)
    {
        if (index >= this.getCharacters(player).size()) return;

        this.setActiveCharacter(player, this.getCharacters(player).get(index));
    }

    public void setActiveCharacter(EntityPlayer player, Character character)
    {
        if (!this.getCharacters(player).contains(character)) return;
        Character active = this.getActiveCharacter(player);
        if (active != null && active.equals(character)) return;

        this.save(player);

        this.activeCharacter.put(player.getUniqueID(), character.getUniqueId());

        this.onSelectCharacter(player, character);

        this.load(player);
    }

    public NBTTagCompound serializeCharacters(EntityPlayer player)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList nbtList = new NBTTagList();

        nbt.setUniqueId("uuid", player.getUniqueID());

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

    public void save(EntityPlayer player)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

        for (IAddon loader : integrations)
        {
            loader.onPreSave(player);
        }

        for (IAddon loader : integrations)
        {
            loader.onSave(player);
        }
    }

    public void load(EntityPlayer player)
    {
        List<IAddon> integrations = MultiCharacterAPI.getAddons();

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

        this.save(player);

        for (IAddon loader : integrations)
        {
            loader.onDisconnect(player);
        }

        for (Character c : this.getCharacters(player))
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
