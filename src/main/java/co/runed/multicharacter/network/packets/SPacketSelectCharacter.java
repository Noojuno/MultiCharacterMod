package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from a client to the server to select a character
 */
public class SPacketSelectCharacter implements IMessage
{
    private int index;

    public SPacketSelectCharacter()
    {

    }

    public SPacketSelectCharacter(int index)
    {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(index);
    }

    public int getIndex()
    {
        return index;
    }

    public static class Handler implements IMessageHandler<SPacketSelectCharacter, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketSelectCharacter message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;
            CharacterManager characterManager = MultiCharacterMod.getCharacterManager();

            // IF PLAYER HAS NO ACTIVE CHARACTER AND INDEX IS -1, KICK PLAYER
            if(message.getIndex() <= -1 && characterManager.getActiveCharacter(player) == null) {
                ctx.getServerHandler().disconnect(new TextComponentString("You must select a character to play"));
                return null;
            }

            // IF VALID INDEX SET ACTIVE CHARACTER
            if(message.getIndex() >= 0) {
                characterManager.setActiveCharacter(player, message.getIndex());
            }

            return null;
        }
    }
}
