package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from a client to the server to select a character
 */
public class C2SPacketSelectCharacter implements IMessage
{
    private int index;
    private Character character;

    public C2SPacketSelectCharacter()
    {

    }

    public C2SPacketSelectCharacter(int index, Character character)
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
        ByteBufUtils.writeTag(buf, character.serializeNBT());
    }

    public int getIndex()
    {
        return index;
    }

    public static class Handler implements IMessageHandler<C2SPacketSelectCharacter, IMessage>
    {
        @Override
        public IMessage onMessage(C2SPacketSelectCharacter message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;
            CharacterManager characterManager = MultiCharacterMod.getCharacterManager();

            // IF PLAYER HAS NO ACTIVE CHARACTER AND INDEX IS -1, KICK PLAYER
            if (message.getIndex() <= -1 && characterManager.getActiveCharacter(player.getUniqueID()) == null)
            {
                ctx.getServerHandler().disconnect(new TextComponentString("You must select a character to play"));
                return null;
            }

            // IF VALID INDEX SET ACTIVE CHARACTER
            if (message.getIndex() >= 0)
            {
                characterManager.setActiveCharacter(player.getUniqueID(), message.getIndex());
            }

            return null;
        }
    }
}
