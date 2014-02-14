package redgear.snowfall.asm;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import redgear.core.util.SimpleItem;
import redgear.core.world.WorldLocation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SnowfallHooks {

	public SnowfallHooks() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void updateTick(World world, int x, int y, int z, Random rand) {
		WorldLocation loc = new WorldLocation(x, y, z, world);

		int meta = loc.getBlockMeta();
		int k1 = meta & 7;

		if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11)
			if (meta < 1)
				loc.setAir();
			else
				loc.placeBlock(new SimpleItem(Blocks.snow_layer, k1 - 1));
		else if (meta < 7 && world.isRaining() && rand.nextInt(8) + 1 > 7 && world.canBlockSeeTheSky(x, y, z))
			loc.placeBlock(new SimpleItem(Blocks.snow_layer, k1 + 1 | meta & -8));
	}

	public static boolean canPlaceBlockAt(World world, int x, int y, int z) {
		WorldLocation loc = new WorldLocation(x, y, z, world).translate(ForgeDirection.DOWN, 1);

		//Block below must be soid, with leaves being an exception. 
		return loc.isSideSolid(ForgeDirection.UP) || loc.getBlock().isLeaves(world, x, y, z);
	}

	public static boolean canSnowAtBody(World world, int x, int y, int z) {
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x, z);
		float f = biomegenbase.getFloatTemperature(x, y, z);

		if (f > 0.15F)
			return false;
		else {
			if (y >= 0 && y < 256 && world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10) {
				Block block = world.getBlock(x, y, z);

				if ((block.isAir(world, x, y, z) || block instanceof BlockBush)
						&& Blocks.snow_layer.canPlaceBlockAt(world, x, y, z))
					return true;
			}

			return false;
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not
	 * BLOCKS
	 */
	public static int onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float par8, float par9, float par10) {
		WorldLocation loc = new WorldLocation(x, y, z, world);

		if (stack.stackSize == 0)
			return 1;

		if (!player.canPlayerEdit(x, y, z, side, stack))
			return 1;

		Block target = loc.getBlock();
		Block snow = Blocks.snow_layer;

		if (target == snow) {
			int meta = loc.getBlockMeta();

			if (meta <= 6) {
				if (world.checkNoEntityCollision(snow.getCollisionBoundingBoxFromPool(world, x, y, z))
						&& world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2)) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, snow.stepSound.soundName,
							(snow.stepSound.getVolume() + 1.0F) / 2.0F, snow.stepSound.getPitch() * 0.8F);
					--stack.stackSize;

					return 0;
				}
			} else
				return 2;
		}

		return 1;
	}

	/**
	 * 
	 * @param world
	 * @param player
	 * @param x
	 * @param y
	 * @param z
	 * @param meta
	 * @return true if the player was holding a snowshovel, false otherwise
	 */
	@SubscribeEvent
	public void snowShovelHook(HarvestDropsEvent event) {
		if (event.block == Blocks.snow || event.block == Blocks.snow_layer) {
			ItemStack heldItem = event.harvester.getHeldItem();

			if (heldItem != null && heldItem.getItem() instanceof ISnowShovel) {
				event.drops.clear();

				if (event.block == Blocks.snow_layer)
					event.drops.add(new ItemStack(Blocks.snow_layer, event.blockMetadata + 1, 0));
				else
					event.drops.add(new ItemStack(Blocks.snow, 1, 0));
			}
		}
	}

	public interface ISnowShovel {

	}
}
