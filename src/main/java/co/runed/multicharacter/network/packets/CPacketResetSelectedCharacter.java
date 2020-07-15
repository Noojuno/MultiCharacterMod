package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.addons.minecraft.MinecraftClientAddon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketResetSelectedCharacter implements IMessage
{
    public CPacketResetSelectedCharacter()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    public static class Handler implements IMessageHandler<CPacketResetSelectedCharacter, IMessage>
    {
        @Override
        public IMessage onMessage(CPacketResetSelectedCharacter message, MessageContext ctx)
        {
            MinecraftClientAddon.selectedCharacter = false;

            return null;
        }
    }
}
