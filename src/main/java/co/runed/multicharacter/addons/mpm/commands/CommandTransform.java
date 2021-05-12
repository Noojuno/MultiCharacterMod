package co.runed.multicharacter.addons.mpm.commands;

import co.runed.multicharacter.addons.mpm.MPMUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

// /customskin <player> <show/hide>
public class CommandTransform extends CommandBase
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        try
        {
            String playerName = params[0];
            String entity = params[1];
            boolean shouldClear = entity.equals("clear");

            if (!shouldClear && !ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(entity)))
            {
                sender.sendMessage(new TextComponentString("Invalid entity '" + new ResourceLocation(entity).toString() + "'"));
                return;
            }

            EntityPlayer player = server.getPlayerList().getPlayerByUsername(playerName);

            if (shouldClear)
            {
                MPMUtil.clearDisguise(player);

                sender.sendMessage(new TextComponentString(player.getName() + "'s transformation has been cleared"));
                return;
            }

            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity));
            MPMUtil.setDisguise(player, (Class<? extends EntityLivingBase>) entityEntry.getEntityClass());

            sender.sendMessage(new TextComponentString(player.getName() + " has been transformed into "
                    + entityEntry.getRegistryName().toString() + " (" + entityEntry.getEntityClass().toString() + ")"));
        }
        catch (Exception e)
        {
            e.printStackTrace();

            sender.sendMessage(new TextComponentString("Error running command."));
        }
    }

    @Override
    public String getName()
    {
        return "transform";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "command.transform.usage";
    }
}
