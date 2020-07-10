package co.runed.multicharacter.events.client;

import co.runed.multicharacter.integration.mpm.SPacketSaveMPM;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.SPacketOpenCharacterGui;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import noppes.mpm.client.gui.GuiMPM;

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
    public void onOpenGui(GuiOpenEvent event)
    {
        if (event.getGui() == null)
        {
            // OPEN CHARACTER SELECTION GUI ON CONNECT (AFTER DOWNLOAD TERRAIN GUI HAS BEEN SHOWN FOR THE FIRST TIME)
            if (previousGui != null && previousGui.getClass() == GuiDownloadTerrain.class && !shownCharacterGui)
            {
                this.shownCharacterGui = true;
                PacketDispatcher.sendToServer(new SPacketOpenCharacterGui());
            }

            // TODO: MOVE TO MPM INTEGRATION
            if (previousGui != null && previousGui.getClass() == GuiMPM.class)
            {
                PacketDispatcher.sendToServer(new SPacketSaveMPM(((GuiMPM) previousGui).playerdata));
            }

            previousGui = event.getGui();
            return;
        }

        previousGui = event.getGui();
    }
}