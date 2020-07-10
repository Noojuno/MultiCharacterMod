package co.runed.multicharacter.events.server;

import co.runed.multicharacter.MultiCharacterMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class PlayerIOEventHandler
{
    @SubscribeEvent
    public void onLoadPlayer(PlayerEvent.LoadFromFile event)
    {
        MultiCharacterMod.getCharacterManager().load(event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onSavePlayer(PlayerEvent.SaveToFile event)
    {
        MultiCharacterMod.getCharacterManager().save(event.getEntityPlayer());
    }
}
