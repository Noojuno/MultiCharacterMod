package co.runed.multicharacter.util;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NbtUtil
{
    public static void writeFile(NBTTagCompound nbt, File directory, String fileName)
    {
        try
        {
            File file1 = new File(directory, fileName + ".dat.tmp");
            File file2 = new File(directory, fileName + ".dat");

            CompressedStreamTools.writeCompressed(nbt, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file1.renameTo(file2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
