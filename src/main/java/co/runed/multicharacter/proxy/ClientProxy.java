package co.runed.multicharacter.proxy;

import co.runed.multicharacter.api.IMultiCharacterIntegration;
import co.runed.multicharacter.api.MultiCharacterAPI;
import co.runed.multicharacter.client.ModKeys;
import co.runed.multicharacter.events.client.ClientGuiEventHandler;
import co.runed.multicharacter.events.client.ClientInputEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        super.init();

        ModKeys.init();
    }

    @Override
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientGuiEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClientInputEventHandler());
    }

    @Override
    public void postInit()
    {
        super.postInit();

        for (IMultiCharacterIntegration integration : MultiCharacterAPI.getIntegrations())
        {
            integration.clientInit();
        }
    }
}
