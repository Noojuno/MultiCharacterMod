package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.network.PacketDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketOpenCharacterGui implements IMessage
{
    public C2SPacketOpenCharacterGui()
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

    public static class Handler implements IMessageHandler<C2SPacketOpenCharacterGui, IMessage>
    {

        @Override
        public IMessage onMessage(C2SPacketOpenCharacterGui message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;

            PacketDispatcher.sendTo(new S2CPacketOpenCharacterGui(MultiCharacterMod.getCharacterManager().getCharacters(player.getUniqueID())), player);

            return null;
        }
    }
}
