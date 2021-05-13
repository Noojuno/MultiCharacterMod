package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SPacketEditCharacter implements IMessage
{
    private int index = -1;
    private Character character;

    public C2SPacketEditCharacter()
    {
    }

    public C2SPacketEditCharacter(int index, Character character)
    {
        this.index = index;
        this.character = character;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        index = buf.readInt();

        character = new Character();
        character.deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(index);
        ByteBufUtils.writeTag(buf, this.character.serializeNBT());
    }

    public Character getCharacter()
    {
        return character;
    }

    public static class Handler implements IMessageHandler<C2SPacketEditCharacter, IMessage>
    {

        @Override
        public IMessage onMessage(C2SPacketEditCharacter message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;

            MultiCharacterMod.getCharacterManager().setCharacter(player.getUniqueID(), message.index, message.character);

            return null;
        }
    }
}
