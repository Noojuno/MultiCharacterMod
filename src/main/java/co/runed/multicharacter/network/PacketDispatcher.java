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
        registerPacket(C2SPacketSelectCharacter.Handler.class, C2SPacketSelectCharacter.class, Side.SERVER);
        registerPacket(C2SPacketCreateCharacter.Handler.class, C2SPacketCreateCharacter.class, Side.SERVER);
        registerPacket(C2SPacketDeleteCharacter.Handler.class, C2SPacketDeleteCharacter.class, Side.SERVER);
        registerPacket(C2SPacketEditCharacter.Handler.class, C2SPacketEditCharacter.class, Side.SERVER);
        registerPacket(C2SPacketOpenCharacterGui.Handler.class, C2SPacketOpenCharacterGui.class, Side.SERVER);
        registerPacket(C2SPacketSaveCharacters.Handler.class, C2SPacketSaveCharacters.class, Side.SERVER);

        registerPacket(S2CPacketOpenCharacterGui.Handler.class, S2CPacketOpenCharacterGui.class, Side.CLIENT);
        registerPacket(S2CPacketResetSelectedCharacter.Handler.class, S2CPacketResetSelectedCharacter.class, Side.CLIENT);
    }

    public static <R extends IMessage, RP extends IMessage> void registerPacket(Class<? extends IMessageHandler<R, RP>> messageHandler, Class<R> requestMessageType, Side side)
    {
        registerPacket(PACKET_ID + 1, messageHandler, requestMessageType, side);
    }

    public static <R extends IMessage, RP extends IMessage> void registerPacket(int id, Class<? extends IMessageHandler<R, RP>> messageHandler, Class<R> requestMessageType, Side side)
    {
        id = id <= PACKET_ID ? PACKET_ID + 1 : id;
        PACKET_ID = id;

        // IF ON A SERVER AND PACKET IS TO BE RUN ON CLIENT, SET HANDLER TO BLANK LAMBDA TO ALLOW USE OF CLIENT SIDE CODE IN PACKET
        if (side == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            WRAPPER.registerMessage((message, ctx) -> null, requestMessageType, id, side);
            return;
        }

        WRAPPER.registerMessage(messageHandler, requestMessageType, id, side);
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