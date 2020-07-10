package co.runed.multicharacter.client.gui;

import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

/**
 * GuiYesNo that always shows the dirt background
 */
public class GuiYesNoBg extends GuiYesNo
{
    public GuiYesNoBg(GuiYesNoCallback parentScreenIn, String messageLine1In, String messageLine2In, int parentButtonClickedIdIn)
    {
        super(parentScreenIn, messageLine1In, messageLine2In, parentButtonClickedIdIn);
    }

    public GuiYesNoBg(GuiYesNoCallback parentScreenIn, String messageLine1In, String messageLine2In, String confirmButtonTextIn, String cancelButtonTextIn, int parentButtonClickedIdIn)
    {
        super(parentScreenIn, messageLine1In, messageLine2In, confirmButtonTextIn, cancelButtonTextIn, parentButtonClickedIdIn);
    }

    @Override
    public void drawWorldBackground(int tint)
    {
        this.drawBackground(tint);
    }
}
