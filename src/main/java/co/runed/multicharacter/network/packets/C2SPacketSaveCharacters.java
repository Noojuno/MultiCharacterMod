package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketSaveCharacters implements IMessage
{
    public C2SPacketSaveCharacters()
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

    public static class Handler implements IMessageHandler<C2SPacketSaveCharacters, IMessage>
    {
        @Override
        public IMessage onMessage(C2SPacketSaveCharacters message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;

            MultiCharacterMod.getCharacterManager().save(player.getUniqueID());

            return null;
        }
    }
}
