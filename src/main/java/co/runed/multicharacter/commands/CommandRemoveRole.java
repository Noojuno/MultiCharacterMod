package co.runed.multicharacter.commands;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// /removerole <player> <index/role>
public class CommandRemoveRole extends CommandBase
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        try {
            String playerName = params[0];
            String role = params[1];

            EntityPlayer player = server.getPlayerList().getPlayerByUsername(playerName);
            Character character = MultiCharacterMod.getCharacterManager().getActiveCharacter(player.getUniqueID());

            boolean success;
            if(role.matches("-?\\d+")) {
                success = character.removeRole(parseInt(role) - 1);
            } else {
                success = character.removeRole(role);
            }

            if(success) {
                sender.sendMessage(new TextComponentString(playerName + "'s role has been removed"));
                return;
            }

            sender.sendMessage(new TextComponentString("Invalid role."));
        }
        catch (Exception e) {
            sender.sendMessage(new TextComponentString("Error running command."));
        }
    }

    @Override
    public String getName() {
        return "removerole";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.removerole.usage";
    }
}
