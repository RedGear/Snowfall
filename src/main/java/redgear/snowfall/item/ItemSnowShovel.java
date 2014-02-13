package redgear.snowfall.item;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import redgear.snowfall.asm.SnowfallHooks.ISnowShovel;

public class ItemSnowShovel extends ItemTool implements ISnowShovel {
	
	private static final Set<Block> breakableBlocks = new HashSet<Block>(2);
	
	static{
		breakableBlocks.add(Blocks.snow);
		breakableBlocks.add(Blocks.snow_layer);
	}
	
	public ItemSnowShovel(ToolMaterial material) {
		super(1, material, breakableBlocks);
		setMaxDamage(material.getMaxUses() * 8);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack stack) {
		return par1Block == Blocks.snow ? true : par1Block == Blocks.snow_layer;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("redgear_snowfall:" + getUnlocalizedName());
	}
}
