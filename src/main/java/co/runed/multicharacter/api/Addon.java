package co.runed.multicharacter.api;

public abstract class Addon implements IAddon
{
    IClientAddon clientAddon;

    @Override
    public IClientAddon getClientAddon()
    {
        return this.clientAddon;
    }

    @Override
    public void setClientAddon(IClientAddon clientAddon)
    {
        this.clientAddon = clientAddon;
    }
}
