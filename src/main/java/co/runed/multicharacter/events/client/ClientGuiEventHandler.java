package co.runed.multicharacter.events.client;

import co.runed.multicharacter.MultiCharacterMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientGuiEventHandler
{
    GuiScreen previousGui = null;

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event)
    {
        if (event.getGui() == null)
        {
            if (previousGui != null) MinecraftForge.EVENT_BUS.post(new GuiCloseEvent(previousGui));

            previousGui = event.getGui();
            return;
        }

        previousGui = event.getGui();
    }
}