package co.runed.multicharacter;

import co.runed.multicharacter.addons.minecraft.MinecraftAddon;
import co.runed.multicharacter.api.MultiCharacterAPI;
import co.runed.multicharacter.character.CharacterManager;
import co.runed.multicharacter.commands.CommandAddRole;
import co.runed.multicharacter.commands.CommandOpenCharacterGui;
import co.runed.multicharacter.commands.CommandRemoveRole;
import co.runed.multicharacter.commands.CommandSetRole;
import co.runed.multicharacter.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
        MultiCharacterAPI.addAddon(new MinecraftAddon());

        MultiCharacterAPI.addAddonIfModLoaded("moreplayermodels", "co.runed.multicharacter.addons.mpm.MPMAddon");

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
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSetRole());
        event.registerServerCommand(new CommandRemoveRole());
        event.registerServerCommand(new CommandAddRole());
        event.registerServerCommand(new CommandOpenCharacterGui());
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
