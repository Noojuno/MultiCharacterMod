package co.runed.multicharacter.network.packets;

import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.client.gui.CharacterListGuiScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class S2CPacketOpenCharacterGui implements IMessage
{
    private List<Character> characters;

    public S2CPacketOpenCharacterGui()
    {

    }

    public S2CPacketOpenCharacterGui(List<Character> characters)
    {
        this.characters = characters;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int size = buf.readInt();
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound nbt = ByteBufUtils.readTag(buf);
            Character character = new Character();
            character.deserializeNBT(nbt);

            chars.add(character);
        }

        this.characters = chars;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.characters.size());
        for (Character character : this.characters)
        {
            ByteBufUtils.writeTag(buf, character.serializeNBT());
        }
    }

    public List<Character> getCharacters()
    {
        return characters;
    }

    public static class Handler implements IMessageHandler<S2CPacketOpenCharacterGui, IMessage>
    {
        @Override
        public IMessage onMessage(S2CPacketOpenCharacterGui message, MessageContext ctx)
        {
            Minecraft mc = Minecraft.getMinecraft();

            mc.addScheduledTask(() -> {
                if (mc.currentScreen != null && mc.currentScreen.getClass() == CharacterListGuiScreen.class) return;

                mc.displayGuiScreen(new CharacterListGuiScreen(null, message.characters));
            });

            return null;
        }
    }
}
