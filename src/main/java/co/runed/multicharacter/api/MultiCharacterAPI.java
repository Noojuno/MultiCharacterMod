package co.runed.multicharacter.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiCharacterAPI
{
    private static final List<IMultiCharacterIntegration> modIntegrations = new ArrayList<>();

    public static File getCharacterDirectory(EntityPlayer player)
    {
        World world = player.world != null ? player.world : FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        File worldDir = world.getSaveHandler().getWorldDirectory();
        File baseDir = new File(worldDir, "characters");
        File dir = new File(baseDir, player.getCachedUniqueIdString());

        if (!dir.exists())
        {
            dir.mkdirs();
        }

        return dir;
    }

    public static void addIntegration(IMultiCharacterIntegration integration)
    {
        modIntegrations.add(integration);
    }

    public static void addIntegrationIfModLoaded(String requiredMod, String className)
    {
        if (Loader.isModLoaded(requiredMod))
        {
            try
            {
                addIntegration(Class.forName(className).asSubclass(IMultiCharacterIntegration.class).newInstance());
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static List<IMultiCharacterIntegration> getIntegrations() {
        return modIntegrations;
    }
}
