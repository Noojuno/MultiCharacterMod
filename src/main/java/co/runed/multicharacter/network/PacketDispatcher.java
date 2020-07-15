package co.runed.multicharacter.network;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.network.packets.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PacketDispatcher
{
    private static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MultiCharacterMod.MODID);
    private static int PACKET_ID = 0;

    public static void init()
    {
        registerPacket(SPacketSelectCharacter.Handler.class, SPacketSelectCharacter.class, Side.SERVER);
        registerPacket(SPacketCreateCharacter.Handler.class, SPacketCreateCharacter.class, Side.SERVER);
        registerPacket(SPacketDeleteCharacter.Handler.class, SPacketDeleteCharacter.class, Side.SERVER);
        registerPacket(SPacketEditCharacter.Handler.class, SPacketEditCharacter.class, Side.SERVER);
        registerPacket(SPacketOpenCharacterGui.Handler.class, SPacketOpenCharacterGui.class, Side.SERVER);
        registerPacket(SPacketSaveCharacters.Handler.class, SPacketSaveCharacters.class, Side.SERVER);

        registerPacket(CPacketOpenCharacterGui.Handler.class, CPacketOpenCharacterGui.class, Side.CLIENT);
        registerPacket(CPacketResetSelectedCharacter.Handler.class, CPacketResetSelectedCharacter.class, Side.CLIENT);
    }

    public static <R extends IMessage, RP extends IMessage> void registerPacket(Class<? extends IMessageHandler<R, RP>> messageHandler, Class<R> requestMessageType, Side side)
    {
        // IF ON A SERVER AND PACKET IS TO BE RUN ON CLIENT, SET HANDLER TO BLANK LAMBDA TO ALLOW USE OF CLIENT SIDE CODE IN PACKET
        if (side == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            WRAPPER.registerMessage((message, ctx) -> null, requestMessageType, PACKET_ID++, side);
            return;
        }

        WRAPPER.registerMessage(messageHandler, requestMessageType, PACKET_ID++, side);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        WRAPPER.sendTo(message, player);
    }

    public static void sendToAll(IMessage message)
    {
        WRAPPER.sendToAll(message);
    }

    public static void sendToAllAround(IMessage message, TargetPoint targetPoint)
    {
        WRAPPER.sendToAllAround(message, targetPoint);
    }

    public static void sendToDimension(IMessage message, int dim)
    {
        WRAPPER.sendToDimension(message, dim);
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(IMessage message)
    {
        WRAPPER.sendToServer(message);
    }
}