package co.runed.multicharacter.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtil
{
    private static Map<UUID, EntityPlayer> playerMap = new HashMap<>();

    public static void addPlayer(EntityPlayer player)
    {
        playerMap.putIfAbsent(player.getUniqueID(), player);
    }

    public static void removePlayer(EntityPlayer player)
    {
        playerMap.remove(player.getUniqueID());
    }

    public static EntityPlayer getPlayerByUUID(UUID uuid)
    {
        EntityPlayer player = null;

        try
        {
            player = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUUID(uuid);

            removePlayer(player);
        }
        catch (NullPointerException e)
        {

        }

        if (player == null && playerMap.containsKey(uuid)) player = playerMap.get(uuid);

        return player;
    }
}
