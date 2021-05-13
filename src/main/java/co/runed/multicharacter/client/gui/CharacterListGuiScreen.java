package co.runed.multicharacter.client.gui;

import co.runed.multicharacter.addons.minecraft.MinecraftClientAddon;
import co.runed.multicharacter.character.Character;
import co.runed.multicharacter.network.PacketDispatcher;
import co.runed.multicharacter.network.packets.C2SPacketCreateCharacter;
import co.runed.multicharacter.network.packets.C2SPacketDeleteCharacter;
import co.runed.multicharacter.network.packets.C2SPacketEditCharacter;
import co.runed.multicharacter.network.packets.C2SPacketSelectCharacter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class CharacterListGuiScreen extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final GuiScreen parentScreen;
    private CharacterSelectionList characterSelectionList;

    private GuiButton btnSelectCharacter;
    private GuiButton btnEditCharacter;
    private GuiButton btnDeleteCharacter;

    private boolean creatingCharacter;
    private boolean editingCharacter;
    private boolean deletingCharacter;

    private int closeState = 0;

    public Character selectedCharacter;

    private boolean initialized;
    final List<Character> characters;

    public CharacterListGuiScreen(GuiScreen parentScreen, List<Character> characters)
    {
        this.parentScreen = parentScreen;
        this.characters = characters;
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (this.initialized)
        {
            this.characterSelectionList.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.initialized = true;

            this.characterSelectionList = new CharacterSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.characterSelectionList.updateCharacters(this.characters);
        }

        this.createButtons();
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.characterSelectionList.handleMouseInput();
    }

    public void createButtons()
    {

        this.btnSelectCharacter = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 152, 20, I18n.format("character.button.select")));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 52, 152, 20, I18n.format("character.button.create")));

        this.btnEditCharacter = this.addButton(new GuiButton(3, this.width / 2 - 154, this.height - 28, 100, 20, I18n.format("selectServer.edit")));
        this.btnDeleteCharacter = this.addButton(new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, I18n.format("selectServer.delete")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 50, this.height - 28, 100, 20, I18n.format("gui.cancel")));

        this.selectCharacter(this.characterSelectionList.getSelected());
    }

    public void updateScreen()
    {
        super.updateScreen();
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (!this.creatingCharacter && !this.deletingCharacter && !this.editingCharacter)
        {
            int characterIndex = this.closeState == 0 ? -1 : this.characters.indexOf(this.selectedCharacter);

            if(characterIndex > -1 && characters.size() <= characterIndex) {
                MinecraftClientAddon.selectedCharacter = true;
            }

            PacketDispatcher.sendToServer(new C2SPacketSelectCharacter(characterIndex));
        }
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            GuiListExtended.IGuiListEntry entry = this.characterSelectionList.getSelected() < 0 ? null : this.characterSelectionList.getListEntry(this.characterSelectionList.getSelected());

            if (button.id == 0 || button.id == 1)
            {
                // CANCEL
                this.closeState = button.id;

                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 2)
            {
                // CREATE CHARACTER
                this.creatingCharacter = true;

                this.selectedCharacter = new Character();
                this.mc.displayGuiScreen(new CharacterEditGuiScreen(this, this.selectedCharacter, false));
            }
            else if (button.id == 3)
            {
                // EDIT CHARACTER
                this.editingCharacter = true;

                this.mc.displayGuiScreen(new CharacterEditGuiScreen(this, this.selectedCharacter, true));
            }
            else if (button.id == 4)
            {
                // DELETE CHARACTER
                String characterName = ((GuiCharacterListEntry) entry).character.getName();

                if (characterName != null)
                {
                    this.deletingCharacter = true;
                    String line1 = I18n.format("character.delete.question");
                    String line2 = "'" + characterName + "' " + I18n.format("character.delete.warning");
                    String confirmButtonText = I18n.format("selectServer.deleteButton");
                    String cancelButtonText = I18n.format("gui.cancel");

                    GuiYesNoBg confirmGui = new GuiYesNoBg(this, line1, line2, confirmButtonText, cancelButtonText, this.characterSelectionList.getSelected());
                    this.mc.displayGuiScreen(confirmGui);
                }
            }
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        GuiCharacterListEntry entry = this.characterSelectionList.getSelected() < 0 ? null : (GuiCharacterListEntry) this.characterSelectionList.getListEntry(this.characterSelectionList.getSelected());

        if (this.deletingCharacter)
        {
            this.deletingCharacter = false;

            if (result)
            {
                // DELETE CHARACTER

                this.characters.remove(this.selectedCharacter);

                PacketDispatcher.sendToServer(new C2SPacketDeleteCharacter(this.selectedCharacter));

                this.characterSelectionList.setSelectedSlotIndex(-1);
                this.characterSelectionList.updateCharacters(this.characters);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.creatingCharacter)
        {
            this.creatingCharacter = false;

            if (result)
            {
                // CREATE CHARACTER

                PacketDispatcher.sendToServer(new C2SPacketCreateCharacter(this.selectedCharacter));

                this.characters.add(this.selectedCharacter);

                this.characterSelectionList.setSelectedSlotIndex(-1);
                this.characterSelectionList.updateCharacters(this.characters);
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.editingCharacter)
        {
            // EDIT CHARACTER
            this.editingCharacter = false;

            if (result)
            {
                int index = this.characters.indexOf(this.selectedCharacter);

                PacketDispatcher.sendToServer(new C2SPacketEditCharacter(index, this.selectedCharacter));

                this.characters.set(index, this.selectedCharacter);

                this.characterSelectionList.updateCharacters(this.characters);
            }

            this.mc.displayGuiScreen(this);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        int i = this.characterSelectionList.getSelected();
        GuiListExtended.IGuiListEntry entry = i < 0 ? null : this.characterSelectionList.getListEntry(i);

        if (i >= 0)
        {
            if (keyCode == Keyboard.KEY_UP)
            {
                if (i > 0)
                {
                    this.selectCharacter(this.characterSelectionList.getSelected() - 1);
                    this.characterSelectionList.scrollBy(-this.characterSelectionList.getSlotHeight());
                }
                else
                {
                    this.selectCharacter(-1);
                }

            }
            else if (keyCode == Keyboard.KEY_DOWN)
            {
                if (i < this.characterSelectionList.getSize())
                {
                    this.selectCharacter(this.characterSelectionList.getSelected() + 1);
                    this.characterSelectionList.scrollBy(this.characterSelectionList.getSlotHeight());
                }
                else
                {
                    this.selectCharacter(-1);
                }

            }
            else if (keyCode != Keyboard.KEY_RETURN && keyCode != Keyboard.KEY_NUMPADENTER)
            {
                super.keyTyped(typedChar, keyCode);

            }
            else
            {
                this.actionPerformed(this.buttonList.get(1));
            }
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.characterSelectionList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("character.header.list"), this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void selectCharacter(int index)
    {
        this.characterSelectionList.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry entry = index < 0 ? null : this.characterSelectionList.getListEntry(index);
        this.btnSelectCharacter.enabled = false;
        this.btnEditCharacter.enabled = false;
        this.btnDeleteCharacter.enabled = false;

        this.selectedCharacter = index >= 0 && index < this.characters.size() ? this.characters.get(index) : null;

        if (entry != null)
        {
            this.btnSelectCharacter.enabled = true;

            this.btnEditCharacter.enabled = true;
            this.btnDeleteCharacter.enabled = true;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.characterSelectionList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        this.characterSelectionList.mouseReleased(mouseX, mouseY, state);
    }
}
