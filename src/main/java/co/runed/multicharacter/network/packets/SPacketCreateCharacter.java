package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketCreateCharacter implements IMessage
{
    private Character character;

    public SPacketCreateCharacter()
    {
        this(new Character());
    }

    public SPacketCreateCharacter(Character character)
    {
        this.character = character;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        character = new Character();
        character.deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.character.serializeNBT());
    }

    public Character getCharacter()
    {
        return character;
    }

    public static class Handler implements IMessageHandler<SPacketCreateCharacter, IMessage>
    {

        @Override
        public IMessage onMessage(SPacketCreateCharacter message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;

            MultiCharacterMod.getCharacterManager().addCharacter(player, message.getCharacter());

            return null;
        }
    }
}
