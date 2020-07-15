package co.runed.multicharacter.commands;

import co.runed.multicharacter.MultiCharacterMod;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.CPacketOpenCharacterGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

// /opencharactergui <player>
public class CommandOpenCharacterGui extends CommandBase
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
    {
        try {
            String playerName = params[0];

            EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(playerName);
            List<Character> characters = MultiCharacterMod.getCharacterManager().getCharacters(player);

            PacketDispatcher.sendTo(new CPacketOpenCharacterGui(characters), player);
            sender.sendMessage(new TextComponentString("Opened character selection for " + playerName));
        }
        catch (Exception e) {
            sender.sendMessage(new TextComponentString("Error running command."));
        }
    }

    @Override
    public String getName() {
        return "opencharactergui";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.opencharactergui.usage";
    }
}
