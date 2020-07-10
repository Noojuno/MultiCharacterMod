package co.runed.multicharacter.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ModKeys
{
    public static final String CATEGORY_MULTICHARACTER = "key.categories.multicharacter";

    public static final KeyBinding KEY_CHANGE_CHARACTER = new KeyBinding("key.character.change", Keyboard.KEY_APOSTROPHE, CATEGORY_MULTICHARACTER);

    public static void init()
    {
        ClientRegistry.registerKeyBinding(KEY_CHANGE_CHARACTER);
    }
}
