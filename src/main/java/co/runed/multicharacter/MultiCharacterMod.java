package co.runed.multicharacter;

import co.runed.multicharacter.api.IMultiCharacterIntegration;
import co.runed.multicharacter.api.MultiCharacterAPI;
import co.runed.multicharacter.character.CharacterManager;
import co.runed.multicharacter.integration.minecraft.MinecraftIntegration;
import co.runed.multicharacter.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MultiCharacterMod.MODID, name = MultiCharacterMod.NAME, version = MultiCharacterMod.VERSION, dependencies = MultiCharacterMod.DEPENDENCIES, useMetadata = true)
public class MultiCharacterMod
{
    public static final String MODID = "multicharacter";
    public static final String NAME = "Multi Character";
    public static final String VERSION = "1.0";
    public static final String DEPENDENCIES = "after:moreplayermodels";

    public static final String COMMON_PROXY = "co.runed.multicharacter.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "co.runed.multicharacter.proxy.ClientProxy";

    private static MultiCharacterMod instance;
    private Logger logger;
    private CharacterManager characterManager;

    @SidedProxy(serverSide = MultiCharacterMod.COMMON_PROXY, clientSide = MultiCharacterMod.CLIENT_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();

        this.characterManager = new CharacterManager();
        MultiCharacterAPI.addIntegration(new MinecraftIntegration());

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();

        MultiCharacterAPI.addIntegrationIfModLoaded("moreplayermodels", "co.runed.multicharacter.integration.mpm.MPMIntegration");

        for (IMultiCharacterIntegration integration : MultiCharacterAPI.getIntegrations())
        {
            integration.init();
        }
    }

    public static MultiCharacterMod getInstance()
    {
        return instance;
    }

    public static Logger getLogger()
    {
        return getInstance().logger;
    }

    public static CharacterManager getCharacterManager()
    {
        return getInstance().characterManager;
    }
}
