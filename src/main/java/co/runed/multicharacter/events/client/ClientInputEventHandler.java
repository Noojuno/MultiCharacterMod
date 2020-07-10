package co.runed.multicharacter.events.client;

import co.runed.multicharacter.client.ModKeys;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.SPacketOpenCharacterGui;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ClientInputEventHandler
{
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (ModKeys.KEY_CHANGE_CHARACTER.isKeyDown())
        {
            PacketDispatcher.sendToServer(new SPacketOpenCharacterGui());
        }
    }
}
