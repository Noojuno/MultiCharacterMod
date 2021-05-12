package co.runed.multicharacter.addons.minecraft;

import co.runed.multicharacter.api.IClientAddon;
import co.runed.multicharacter.events.client.GuiCloseEvent;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.C2SPacketOpenCharacterGui;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class MinecraftClientAddon implements IClientAddon
{
    public static boolean selectedCharacter = false;
    public static boolean shownCharacterGui = false;

    @Override
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void clientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        shownCharacterGui = false;
        selectedCharacter = false;
    }

    @SubscribeEvent
    public void onCloseGui(GuiCloseEvent event)
    {
        GuiScreen gui = event.getGui();

        // OPEN CHARACTER SELECTION GUI ON CONNECT (AFTER DOWNLOAD TERRAIN GUI HAS BEEN SHOWN FOR THE FIRST TIME)
        if (gui != null && gui.getClass() == GuiDownloadTerrain.class && (!shownCharacterGui || !selectedCharacter))
        {
            shownCharacterGui = true;
            PacketDispatcher.sendToServer(new C2SPacketOpenCharacterGui());
        }
    }
}
