package co.runed.multicharacter.events.client;

import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.SPacketOpenCharacterGui;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ClientGuiEventHandler
{
    GuiScreen previousGui = null;
    boolean shownCharacterGui = false;

    @SubscribeEvent
    public void clientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        this.shownCharacterGui = false;
    }

    @SubscribeEvent
    public void onCloseGui(GuiCloseEvent event)
    {
        GuiScreen gui = event.getGui();

        // OPEN CHARACTER SELECTION GUI ON CONNECT (AFTER DOWNLOAD TERRAIN GUI HAS BEEN SHOWN FOR THE FIRST TIME)
        if (gui != null && gui.getClass() == GuiDownloadTerrain.class && !shownCharacterGui)
        {
            this.shownCharacterGui = true;
            PacketDispatcher.sendToServer(new SPacketOpenCharacterGui());
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event)
    {
        if (event.getGui() == null)
        {
            MinecraftForge.EVENT_BUS.post(new GuiCloseEvent(previousGui));

            previousGui = event.getGui();
            return;
        }

        previousGui = event.getGui();
    }
}