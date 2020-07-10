package co.runed.multicharacter.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiCharacterAPI
{
    private static final List<IAddon> addons = new ArrayList<>();

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

    public static void initAddons()
    {
        for (IAddon addon : addons)
        {
            addon.init();
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            for (IAddon addon : addons)
            {
                if (addon.getClientAddon() != null)
                {
                    addon.getClientAddon().init();
                }
            }
        }
    }

    public static void addAddon(IAddon addon)
    {
        addons.add(addon);
    }

    public static void addAddonIfModLoaded(String requiredMod, String className)
    {
        if (Loader.isModLoaded(requiredMod))
        {
            try
            {
                addAddon(Class.forName(className).asSubclass(IAddon.class).newInstance());
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static List<IAddon> getAddons()
    {
        return addons;
    }
}
