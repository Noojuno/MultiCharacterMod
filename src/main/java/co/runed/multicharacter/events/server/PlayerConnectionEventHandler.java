package co.runed.multicharacter.events.server;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.S2CPacketResetSelectedCharacter;
import co.runed.multicharacter.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerConnectionEventHandler
{
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;

        PlayerUtil.addPlayer(event.player);

        if (MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID()) != null) return;

        player.setGameType(GameType.SPECTATOR);

        PacketDispatcher.sendTo(new S2CPacketResetSelectedCharacter(), (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        PlayerUtil.removePlayer(event.player);

        MultiCharacterMod.getCharacterManager().onDisconnect(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        MultiCharacterMod.getCharacterManager().onChangeDimension(event.player, event.fromDim, event.toDim);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        MultiCharacterMod.getCharacterManager().onRespawn(event.player);
    }
}
