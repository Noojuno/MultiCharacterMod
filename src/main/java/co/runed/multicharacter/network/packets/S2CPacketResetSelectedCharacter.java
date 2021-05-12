package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.addons.minecraft.MinecraftClientAddon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPacketResetSelectedCharacter implements IMessage
{
    public S2CPacketResetSelectedCharacter()
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

    public static class Handler implements IMessageHandler<S2CPacketResetSelectedCharacter, IMessage>
    {
        @Override
        public IMessage onMessage(S2CPacketResetSelectedCharacter message, MessageContext ctx)
        {
            MinecraftClientAddon.selectedCharacter = false;

            return null;
        }
    }
}
