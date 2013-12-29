package redgear.snowfall;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemTool;

public class ItemSnowShovel extends ItemTool
{
    public ItemSnowShovel(int id, EnumToolMaterial material)
    {
        super(id, 1, material, new Block[] {Block.snow, Block.blockSnow});
        this.setMaxDamage(material.getMaxUses() * 8);
        setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean canHarvestBlock(Block par1Block)
    {
        return par1Block == Block.snow ? true : par1Block == Block.blockSnow;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("redgear_snowfall:" + getUnlocalizedName());
    }
}
