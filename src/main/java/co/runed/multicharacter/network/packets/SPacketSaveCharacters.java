package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSaveCharacters implements IMessage
{
    public SPacketSaveCharacters()
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

    public static class Handler implements IMessageHandler<SPacketSaveCharacters, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketSaveCharacters message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;

            MultiCharacterMod.getCharacterManager().save(player);

            return null;
        }
    }
}
