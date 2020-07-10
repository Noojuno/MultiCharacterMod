package co.runed.multicharacter.integration.mpm;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.api.IMultiCharacterIntegration;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.character.CharacterManager;
import co.runed.multicharacter.events.client.ClientInputEventHandler;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.util.Scheduler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import noppes.mpm.LogWriter;
import noppes.mpm.ModelData;
import noppes.mpm.Server;
import noppes.mpm.constants.EnumPackets;

public class MPMIntegration implements IMultiCharacterIntegration
{
    public static final String NBT_DATA_KEY = "MorePlayerModels";

    @Override
    public void init()
    {
        PacketDispatcher.registerPacket(CPacketUpdateMPM.Handler.class, CPacketUpdateMPM.class, Side.CLIENT);
        PacketDispatcher.registerPacket(SPacketSaveMPM.Handler.class, SPacketSaveMPM.class, Side.SERVER);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void clientInit()
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

        NBTTagCompound additionalNbt = characterManager.getActiveCharacter(player).getNbt();

        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        data.displayName = activeCharacter.getName();
        data.player = player;

        MPMUtil.setModelData(player, data);

        additionalNbt.setTag(NBT_DATA_KEY, data.writeToNBT());
        activeCharacter.setNbt(additionalNbt);
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
        CharacterManager characterManager = MultiCharacterMod.getCharacterManager();

        ModelData data = player.getCapability(ModelData.MODELDATA_CAPABILITY, null);
        data.readFromNBT(new NBTTagCompound());
        data.displayName = character.getName();

        MPMUtil.setModelData(player, data);
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
