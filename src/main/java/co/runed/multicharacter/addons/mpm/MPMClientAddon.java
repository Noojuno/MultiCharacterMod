package co.runed.multicharacter.addons.mpm;

import co.runed.multicharacter.addons.mpm.packets.SPacketSaveMPM;
import co.runed.multicharacter.api.ClientAddon;
import co.runed.multicharacter.events.client.GuiCloseEvent;
import co.runed.multicharacter.network.PacketDispatcher;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.mpm.client.gui.GuiMPM;

public class MPMClientAddon extends ClientAddon
{
    @Override
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCloseGui(GuiCloseEvent event)
    {
        GuiScreen gui = event.getGui();

        // TODO: MOVE TO MPM INTEGRATION
        if (gui != null && gui.getClass() == GuiMPM.class)
        {
            PacketDispatcher.sendToServer(new SPacketSaveMPM(((GuiMPM) gui).playerdata));
        }
    }
}
