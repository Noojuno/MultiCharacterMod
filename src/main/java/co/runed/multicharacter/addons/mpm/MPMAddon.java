package co.runed.multicharacter.addons.mpm;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.addons.mpm.packets.S2CPacketUpdateMPM;
import co.runed.multicharacter.addons.mpm.packets.C2SPacketSaveMPM;
import co.runed.multicharacter.api.Addon;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.character.CharacterManager;
import co.runed.multicharacter.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import noppes.mpm.ModelData;

public class MPMAddon extends Addon
{
    public static final String NBT_DATA_KEY = "MorePlayerModels";
    public static final String MPM_EXTRA_KEY = "mpm_extra";
    public static final String IS_DISGUISED_KEY = "is_disguised";
    public static final String SKIN_URL_KEY = "url";
    public static final String REGULAR_ENTITY_KEY = "regular_entity";

    @Override
    public void init()
    {
        this.setClientAddon(new MPMClientAddon());

        PacketDispatcher.registerPacket(S2CPacketUpdateMPM.Handler.class, S2CPacketUpdateMPM.class, Side.CLIENT);
        PacketDispatcher.registerPacket(C2SPacketSaveMPM.Handler.class, C2SPacketSaveMPM.class, Side.SERVER);
    }

    @Override
    public void onPreLoad(EntityPlayer player)
    {

    }

    @Override
    public void onLoad(EntityPlayer player)
    {
        CharacterManager characterManager = MultiCharacterMod.getCharacterManager();
        Character activeCharacter = characterManager.getActiveCharacter(player);

        if (activeCharacter == null) return;

        player.world.getGameRules().setOrCreateGameRule("mpmAllowEntityModels", "true");

        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        NBTTagCompound characterNbt = activeCharacter.getNbt();

        if (characterNbt.hasKey(NBT_DATA_KEY))
        {
            NBTTagCompound mpmNbt = characterNbt.getCompoundTag(NBT_DATA_KEY);
            data.readFromNBT(mpmNbt);
        }

        data.displayName = activeCharacter.getName();

        MPMUtil.setModelData(player, data);
    }

    @Override
    public void onPreSave(EntityPlayer player)
    {
        CharacterManager characterManager = MultiCharacterMod.getCharacterManager();
        Character activeCharacter = characterManager.getActiveCharacter(player);

        if (activeCharacter == null) return;

        NBTTagCompound characterNbt = characterManager.getActiveCharacter(player).getNbt();

        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        data.displayName = activeCharacter.getName();
        data.player = player;

        MPMUtil.setModelData(player, data);

        NBTTagCompound extra = characterNbt.getCompoundTag(MPM_EXTRA_KEY);
        if (extra.hasKey(IS_DISGUISED_KEY))
        {
            if (!extra.getBoolean(IS_DISGUISED_KEY))
            {
                extra.setString(SKIN_URL_KEY, data.url);
                if (data.entityClass != null) extra.setString(REGULAR_ENTITY_KEY, data.entityClass.toString());
            }
        }

        characterNbt.setTag(NBT_DATA_KEY, data.writeToNBT());

        activeCharacter.setNbt(characterNbt);
    }

    @Override
    public void onSave(EntityPlayer player)
    {

    }

    @Override
    public void onDisconnect(EntityPlayer player)
    {
        // RESET MODELDATA
        MPMUtil.resetModelData(player);
    }

    @Override
    public void onCreateCharacter(EntityPlayer player, Character character)
    {
        NBTTagCompound nbt = character.getNbt();

        ModelData data = new ModelData();
        data.displayName = character.getName();
        nbt.setTag(NBT_DATA_KEY, data.writeToNBT());

        character.setNbt(nbt);
    }

    @Override
    public void onSelectCharacter(EntityPlayer player, Character character)
    {
        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        data.readFromNBT(new NBTTagCompound());
        data.displayName = character.getName();

        MPMUtil.setModelData(player, data);
    }

    @Override
    public void onPostSelectCharacter(EntityPlayer player, Character character)
    {
        NBTTagCompound additionalNbt = character.getNbt();

        NBTTagCompound extra = additionalNbt.getCompoundTag(MPM_EXTRA_KEY);
        if (extra.hasKey(IS_DISGUISED_KEY))
        {
            if (extra.getBoolean(IS_DISGUISED_KEY))
            {
                try
                {
                    MPMUtil.clearDisguise(player);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onChangeDimension(EntityPlayer player, int fromDim, int toDim)
    {
        // MAKE SURE TO SYNC MORE PLAYER MODELS INFO ON DIM CHANGE
        this.onLoad(player);
    }

    @Override
    public void onRespawn(EntityPlayer player)
    {
        // MAKE SURE TO SYNC MORE PLAYER MODELS INFO ON RESPAWN
        this.onLoad(player);
    }
}
