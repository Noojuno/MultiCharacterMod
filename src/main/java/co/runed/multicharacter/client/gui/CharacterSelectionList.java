package co.runed.multicharacter.client.gui;

import co.runed.multicharacter.character.Character;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class CharacterSelectionList extends GuiListExtended
{
    private final CharacterListGuiScreen parent;
    private final List<GuiCharacterListEntry> characterList = Lists.newArrayList();
    private int selectedSlotIndex = -1;

    public CharacterSelectionList(CharacterListGuiScreen parent, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.parent = parent;
    }

    public GuiListExtended.IGuiListEntry getListEntry(int index)
    {
        if (index < this.characterList.size() && index >= 0)
        {
            return this.characterList.get(index);
        }

        return this.characterList.get(0);
    }

    protected int getSize()
    {
        return this.characterList.size();
    }

    public void setSelectedSlotIndex(int selectedSlotIndexIn)
    {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }

    protected boolean isSelected(int slotIndex)
    {
        return slotIndex == this.selectedSlotIndex;
    }

    public int getSelected()
    {
        return this.selectedSlotIndex;
    }

    public void updateCharacters(List<Character> characters)
    {
        this.characterList.clear();

        for (int i = 0; i < characters.size(); ++i)
        {
            this.characterList.add(new GuiCharacterListEntry(this.parent, characters.get(i)));
        }
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 30;
    }

    public int getListWidth()
    {
        return super.getListWidth() + 85;
    }
}