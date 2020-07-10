package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.network.PacketDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketOpenCharacterGui implements IMessage
{
    public SPacketOpenCharacterGui()
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

    public static class Handler implements IMessageHandler<SPacketOpenCharacterGui, IMessage>
    {

        @Override
        public IMessage onMessage(SPacketOpenCharacterGui message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;

            PacketDispatcher.sendTo(new CPacketOpenCharacterGui(MultiCharacterMod.getCharacterManager().getCharacters(player)), player);

            return null;
        }
    }
}
