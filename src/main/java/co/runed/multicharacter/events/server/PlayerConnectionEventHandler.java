package co.runed.multicharacter.events.server;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.integration.mpm.MPMUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.mpm.ModelData;

public class PlayerConnectionEventHandler
{
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;

        if (MultiCharacterMod.getCharacterManager().getActiveCharacter(player) != null) return;

        player.setGameType(GameType.SPECTATOR);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        MultiCharacterMod.getCharacterManager().onDisconnect(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        MultiCharacterMod.getCharacterManager().onChangeDimension(event.player, event.fromDim, event.toDim);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        MultiCharacterMod.getCharacterManager().onRespawn(event.player);
    }
}
