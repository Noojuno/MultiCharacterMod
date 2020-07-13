package co.runed.multicharacter.character;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Character implements INBTSerializable<NBTTagCompound>
{
    private UUID uniqueId = UUID.randomUUID();
    private String name;
    private List<String> roles = new ArrayList<>();
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

    public void addRole(String role)
    {
        this.roles.add(role);
    }

    public void setRole(int index, String role)
    {
        if(index <= this.roles.size()) {
            this.roles.set(index, role);
            return;
        }

        for (int i = 0; i < index + 1; i++)
        {
            if (index >= this.roles.size() && i < index)
            {
                this.roles.add("");
            }

            if (i == index)
            {
                this.roles.set(i, role);
                return;
            }
        }
    }

    public boolean removeRole(int index)
    {
        if (index < this.roles.size())
        {
            this.roles.remove(index);
            return true;
        }

        return false;
    }

    public boolean removeRole(String role)
    {
        for (int i = 0; i < this.roles.size(); i++)
        {
            String r = this.roles.get(i);

            if (r.equals(role))
            {
                this.roles.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean hasRole(String role)
    {
        return this.roles.contains(role);
    }

    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }

    public List<String> getRoles()
    {
        return roles;
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

        NBTTagList roleList = new NBTTagList();

        for (String role : this.roles)
        {
            roleList.appendTag(new NBTTagString(role));
        }

        tag.setTag("roles", roleList);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.setUniqueId(nbt.getUniqueId("uuid"));
        this.setName(nbt.getString("name"));

        if (nbt.hasKey("roles"))
        {
            NBTTagList roleList = nbt.getTagList("roles", Constants.NBT.TAG_STRING);

            for (int i = 0; i < roleList.tagCount(); i++)
            {
                String role = roleList.getStringTagAt(i);

                this.addRole(role);
            }
        }

        this.nbt = nbt.getCompoundTag("nbt");
    }
}
