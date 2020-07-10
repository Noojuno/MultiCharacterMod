package co.runed.multicharacter.client.gui;

import co.runed.multicharacter.character.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class CharacterEditGuiScreen extends GuiScreen
{
    private final CharacterListGuiScreen lastScreen;
    private final Character character;
    private final boolean editMode;
    private GuiTextField characterNameTextField;

    public CharacterEditGuiScreen(CharacterListGuiScreen lastScreen, Character character, boolean editMode)
    {
        this.lastScreen = lastScreen;
        this.character = character;
        this.editMode = editMode;
    }

    @Override
    public void updateScreen()
    {
        if (this.characterNameTextField == null) return;

        this.characterNameTextField.updateCursorCounter();
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));

        this.characterNameTextField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
        this.characterNameTextField.setMaxStringLength(128);
        this.characterNameTextField.setFocused(true);

        if (character.getName() != null)
        {
            this.characterNameTextField.setText(character.getName());
        }
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.lastScreen.confirmClicked(false, 1);
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
            else if (button.id == 0)
            {
                character.setName(this.characterNameTextField.getText());

                boolean result = !this.characterNameTextField.getText().isEmpty();

                this.lastScreen.confirmClicked(result, 0);
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.characterNameTextField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.characterNameTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        String header = this.editMode ? I18n.format("character.header.edit") : I18n.format("character.header.create");

        this.drawBackground(0);
        this.drawCenteredString(this.fontRenderer, header, this.width / 2, 20, 0xFFFFFF);
        this.drawString(this.fontRenderer, I18n.format("character.name"), this.width / 2 - 100, 100, 0xA0A0A0);
        this.characterNameTextField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
