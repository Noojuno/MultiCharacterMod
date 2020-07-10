package co.runed.multicharacter.integration.mpm;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.SPacketSaveCharacters;
import co.runed.multicharacter.util.Scheduler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import noppes.mpm.ModelData;
import noppes.mpm.MorePlayerModels;
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

        if (player instanceof EntityPlayerMP) {
            Character character = MultiCharacterMod.getCharacterManager().getActiveCharacter(player);

            NBTTagCompound nbt = character.getNbt();
            nbt.setTag(MPMIntegration.NBT_DATA_KEY, data.writeToNBT());
            character.setNbt(nbt);
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
            PacketDispatcher.sendTo(new CPacketUpdateMPM(data), (EntityPlayerMP) player);
            Server.sendAssociatedData(player, EnumPackets.SEND_PLAYER_DATA, player.getUniqueID(), data.writeToNBT());
        }
    }
}
