package co.runed.multicharacter.events.server;

import co.runed.multicharacter.MultiCharacterMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerIOEventHandler
{
    @SubscribeEvent
    public void onLoadPlayer(PlayerEvent.LoadFromFile event)
    {
        MultiCharacterMod.getCharacterManager().load(event.getEntityPlayer().getUniqueID());
    }

    @SubscribeEvent
    public void onSavePlayer(PlayerEvent.SaveToFile event)
    {
        MultiCharacterMod.getCharacterManager().save(event.getEntityPlayer().getUniqueID());
    }
}
