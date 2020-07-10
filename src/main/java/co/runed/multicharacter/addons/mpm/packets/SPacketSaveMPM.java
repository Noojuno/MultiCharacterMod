package co.runed.multicharacter.addons.mpm.packets;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.addons.mpm.MPMUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noppes.mpm.ModelData;

public class SPacketSaveMPM implements IMessage
{
    public ModelData modelData;

    public SPacketSaveMPM()
    {

    }

    public SPacketSaveMPM(ModelData data)
    {
        this.modelData = data;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        NBTTagCompound nbt = ByteBufUtils.readTag(buf);

        this.modelData = new ModelData();
        this.modelData.readFromNBT(nbt);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.modelData.writeToNBT());
    }

    public static class Handler implements IMessageHandler<SPacketSaveMPM, IMessage>
    {
        @Override
        public IMessage onMessage(SPacketSaveMPM message, MessageContext ctx)
        {
            ModelData data = message.modelData;

            MPMUtil.setModelData(ctx.getServerHandler().player, data);
            MultiCharacterMod.getCharacterManager().save(ctx.getServerHandler().player);

            return null;
        }
    }
}

