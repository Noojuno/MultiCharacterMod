package co.runed.multicharacter.addons.mpm;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.addons.mpm.packets.S2CPacketUpdateMPM;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.util.Scheduler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import noppes.mpm.ModelData;
import noppes.mpm.Server;
import noppes.mpm.client.Client;
import noppes.mpm.client.ClientProxy;
import noppes.mpm.constants.EnumPackets;

public class MPMUtil
{
    public static ModelData setModelData(EntityPlayer player, ModelData modelData)
    {
        modelData.player = player;
        modelData.save();

        NBTTagCompound capNBT = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(capNBT);

        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        data.readFromNBT(modelData.writeToNBT());
        data.player = player;
        data.save();

        if (player instanceof EntityPlayerMP)
        {
            Character character = MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID());

            if (character != null)
            {
                NBTTagCompound nbt = character.getNbt();
                nbt.setTag(MPMAddon.NBT_DATA_KEY, data.writeToNBT());
                character.setNbt(nbt);
            }
        }

        Scheduler.run(() -> syncModelData(player, data), 100);

        return data;
    }

    public static ModelData resetModelData(EntityPlayer player)
    {
        return setModelData(player, new ModelData());
    }

    public static void syncModelData(EntityPlayer player, ModelData data)
    {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();

        data.player = player;

        if (effectiveSide == Side.CLIENT)
        {
            Client.sendData(EnumPackets.UPDATE_PLAYER_DATA, data.writeToNBT());
            ClientProxy.fixModels(false);
        }
        else
        {
            PacketDispatcher.sendTo(new S2CPacketUpdateMPM(data), (EntityPlayerMP) player);
            Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID(), data.writeToNBT());
        }
    }

    public static void clearDisguise(EntityPlayer player) throws ClassNotFoundException
    {
        Character character = MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID());
        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);

        NBTTagCompound nbt = character.getNbt();
        NBTTagCompound extra = nbt.getCompoundTag(MPMAddon.MPM_EXTRA_KEY);
        String url = extra.getString(MPMAddon.SKIN_URL_KEY);

        extra.setBoolean(MPMAddon.IS_DISGUISED_KEY, false);

        String regularEntity = extra.getString(MPMAddon.REGULAR_ENTITY_KEY);
        if (regularEntity.isEmpty())
        {
            data.clearEntity();
            data.entityClass = null;
        }
        else
        {
            data.setEntityClass((Class<? extends EntityLivingBase>) Class.forName(regularEntity));
        }

        data.url = url;
        extra.setString(MPMAddon.SKIN_URL_KEY, "");
        extra.setBoolean(MPMAddon.IS_DISGUISED_KEY, false);

        data.resourceInit = false;
        data.resourceLoaded = false;

        nbt.setTag(MPMAddon.MPM_EXTRA_KEY, extra);
        character.setNbt(nbt);

        MPMUtil.setModelData(player, data);
    }

    public static void setDisguise(EntityPlayer player, Class<? extends EntityLivingBase> entityClass)
    {
        Character character = MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID());
        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);

        NBTTagCompound nbt = character.getNbt();
        NBTTagCompound extra = nbt.getCompoundTag(MPMAddon.MPM_EXTRA_KEY);
        boolean isDisguised = extra.getBoolean(MPMAddon.IS_DISGUISED_KEY);

        extra.setBoolean(MPMAddon.IS_DISGUISED_KEY, true);

        if (!isDisguised)
        {
            extra.setString(MPMAddon.SKIN_URL_KEY, data.url);
            if (data.entityClass != null) extra.setString(MPMAddon.REGULAR_ENTITY_KEY, data.entityClass.toString());
        }

        data.url = "";
        data.setEntityClass(entityClass);

        extra.setBoolean(MPMAddon.IS_DISGUISED_KEY, true);

        data.resourceInit = false;
        data.resourceLoaded = false;

        nbt.setTag(MPMAddon.MPM_EXTRA_KEY, extra);
        character.setNbt(nbt);

        MPMUtil.setModelData(player, data);
    }
}
