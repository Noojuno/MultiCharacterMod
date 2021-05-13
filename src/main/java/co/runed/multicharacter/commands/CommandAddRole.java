package co.runed.multicharacter.commands;

import co.runed.multicharacter.MultiCharacterMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandAddRole extends CommandBase
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        try {
            String playerName = params[0];
            String role = params[1];

            EntityPlayer player = server.getPlayerList().getPlayerByUsername(playerName);

            MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID()).addRole(role);

            sender.sendMessage(new TextComponentString("Role '" + role + "' has been added to " + playerName));
        }
        catch (Exception e) {
            sender.sendMessage(new TextComponentString("Error running command."));
        }
    }

    @Override
    public String getName() {
        return "addrole";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.addrole.usage";
    }
}
