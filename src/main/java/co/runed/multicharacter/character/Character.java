package co.runed.multicharacter.character;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class Character implements INBTSerializable<NBTTagCompound>
{
    private UUID uniqueId = UUID.randomUUID();
    private String name;
    private NBTTagCompound nbt = new NBTTagCompound();

    public Character()
    {
        this("");
    }

    public Character(String name)
    {
        this.setName(name);
    }

    public UUID getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public NBTTagCompound getNbt()
    {
        return this.nbt;
    }

    public void setNbt(NBTTagCompound nbt)
    {
        this.nbt = nbt;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId("uuid", this.uniqueId);
        tag.setString("name", this.getName());
        tag.setTag("nbt", this.nbt);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.setUniqueId(nbt.getUniqueId("uuid"));
        this.setName(nbt.getString("name"));

        this.nbt = nbt.getCompoundTag("nbt");
    }
}
