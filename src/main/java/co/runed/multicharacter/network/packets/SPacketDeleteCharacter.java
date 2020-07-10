package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketDeleteCharacter implements IMessage
{
    private Character character;

    public SPacketDeleteCharacter()
    {
    }

    public SPacketDeleteCharacter(Character character)
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
        NBTTagCompound nbt = new NBTTagCompound();

        if (this.character != null) nbt = this.character.serializeNBT();

        ByteBufUtils.writeTag(buf, nbt);
    }

    public Character getCharacter()
    {
        return character;
    }

    public static class Handler implements IMessageHandler<SPacketDeleteCharacter, IMessage>
    {

        @Override
        public IMessage onMessage(SPacketDeleteCharacter message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;

            MultiCharacterMod.getCharacterManager().removeCharacter(player, message.getCharacter());
            return null;
        }
    }
}
