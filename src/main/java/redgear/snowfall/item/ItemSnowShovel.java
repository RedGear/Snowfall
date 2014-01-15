package redgear.snowfall.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemTool;
import redgear.snowfall.asm.SnowfallHooks.ISnowShovel;

public class ItemSnowShovel extends ItemTool implements ISnowShovel {
	public ItemSnowShovel(int id, EnumToolMaterial material) {
		super(id, 1, material, new Block[] {Block.snow, Block.blockSnow });
		setMaxDamage(material.getMaxUses() * 8);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public boolean canHarvestBlock(Block par1Block) {
		return par1Block == Block.snow ? true : par1Block == Block.blockSnow;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("redgear_snowfall:" + getUnlocalizedName());
	}
}
