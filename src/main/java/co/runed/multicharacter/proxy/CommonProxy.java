package co.runed.multicharacter.proxy;

import co.runed.multicharacter.events.server.PlayerConnectionEventHandler;
import co.runed.multicharacter.events.server.PlayerIOEventHandler;
import co.runed.multicharacter.network.PacketDispatcher;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{

    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new PlayerIOEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerConnectionEventHandler());

        PacketDispatcher.init();
    }

    public void init()
    {

    }

    public void postInit()
    {

    }
}
