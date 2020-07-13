package co.runed.multicharacter.commands;

import co.runed.multicharacter.MultiCharacterMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandSetRole extends CommandBase
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        try {
            String playerName = params[0];
            int index = parseInt(params[1]);
            String role = params[2];

            EntityPlayer player = server.getPlayerList().getPlayerByUsername(playerName);

            MultiCharacterMod.getCharacterManager().getActiveCharacter(player).setRole(index - 1, role);

            sender.sendMessage(new TextComponentString(playerName + "'s role has been set to '" + role + "'"));
        }
        catch (Exception e) {
            sender.sendMessage(new TextComponentString("Error running command."));
        }
    }

    @Override
    public String getName() {
        return "setrole";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.setrole.usage";
    }
}
