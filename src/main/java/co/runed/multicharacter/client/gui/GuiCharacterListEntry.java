package co.runed.multicharacter.client.gui;

import co.runed.multicharacter.character.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharacterListEntry implements GuiListExtended.IGuiListEntry
{
    public final CharacterListGuiScreen parent;
    public final Character character;
    public final Minecraft mc;
    public GuiButton guiButton;

    protected GuiCharacterListEntry(CharacterListGuiScreen parentScreen, Character character)
    {
        this.parent = parentScreen;
        this.character = character;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks)
    {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
    {
        this.mc.fontRenderer.drawString(this.character.getName(), x + 3, y + 1, 0xFFFFFF);
        this.mc.fontRenderer.drawString("Role 1, Role 2, Role 3", x + 3, y + this.mc.fontRenderer.FONT_HEIGHT + 1, 0x808080);

        //this.mc.fontRenderer.drawString(list.get(i), x + 3, y + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 0x808080);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
    {
        this.parent.selectCharacter(slotIndex);

        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {

    }
}
