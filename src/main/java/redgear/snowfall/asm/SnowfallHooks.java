package redgear.snowfall.asm;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
		//else

		//if (meta < 7 && world.isRaining() && rand.nextInt(8) + 1 > 7 && world.canBlockSeeTheSky(x, y, z))
		//	loc.placeBlock(new SimpleItem(Blocks.snow_layer, k1 + 1 | meta & -8));
	}

	public static boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return canPlaceBlockAt(new WorldLocation(x, y, z, world));
	}

	//Block below must be soid, with leaves being an exception. 
	public static boolean canPlaceBlockAt(WorldLocation loc) {
		if (loc.getBlock() == Blocks.snow_layer || loc.isAir() || loc.getMaterial() == Material.plants) {//This block

			loc = loc.translate(ForgeDirection.DOWN, 1);

			return loc.isSideSolid(ForgeDirection.UP)//Block below.
					|| loc.getBlock().isLeaves(loc.world, loc.getX(), loc.getY(), loc.getZ())
					|| loc.getBlock() == Blocks.farmland;
		} else
			return false;
	}

	public static boolean canSnowAtBody(World world, int x, int y, int z, boolean checkLight) {
		WorldLocation loc = new WorldLocation(x, y, z, world);
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x, z);
		float f = biomegenbase.getFloatTemperature(x, y, z);

		if (f > 0.15F)
			return false;
		else if (!checkLight)
			return true;
		else 
			if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10) {
				Block block = loc.getBlock();

				if (block == Blocks.snow_layer) { //grow the snow. 
					int smallest = loc.getBlockMeta();

					if (smallest == 7) {
						if (Snowfall.deepSnow || Snowfall.iceAge) {
							loc = loc.translate(-1, 0, -1);

							for (int i = 0; i < 3; i++)
								for (int j = 0; j < 3; j++)
									deepSnow(loc.translate(i, 0, j));
						}
						return false;
					}

					if (world.rand.nextInt(smallest + 2) == 0) {
						loc = loc.translate(-1, 0, -1);

						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 3; j++)
								smallest = findSmallest(loc.translate(i, 0, j), smallest);

						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 3; j++)
								grow(loc.translate(i, 0, j), smallest);
					}
					return false;

				}

				return canPlaceBlockAt(world, x, y, z);
			}
			else
				return false;
	}

	private static void deepSnow(WorldLocation loc) {
		if (loc.getBlockMeta() == 7) {
			if (Snowfall.deepSnow && !Snowfall.iceAge)
				if (!checkDeepSnow(loc.translate(ForgeDirection.DOWN, 2)))
					loc.placeBlock(new SimpleItem(Blocks.snow));

			if (Snowfall.iceAge) {
				if (!(loc.translate(ForgeDirection.DOWN, 6).getBlock() == Blocks.packed_ice))
					loc.placeBlock(new SimpleItem(Blocks.snow));

				loc = loc.translate(ForgeDirection.DOWN, 3);
				if (loc.getBlock() == Blocks.snow) {
					loc.placeBlock(new SimpleItem(Blocks.ice));

					loc = loc.translate(ForgeDirection.DOWN, 2);
					if (loc.getBlock() == Blocks.ice)
						loc.placeBlock(new SimpleItem(Blocks.packed_ice));

				}

			}
		}
	}

	private static boolean checkDeepSnow(WorldLocation loc) {
		Material mat = loc.getMaterial();
		return mat == Material.snow ? true : mat == Material.craftedSnow ? true : mat == Material.ice ? true
				: mat == Material.packedIce;
	}

	private static int findSmallest(WorldLocation loc, int smallest) {
		if (loc.getBlock() == Blocks.snow_layer)
			return Math.min(loc.getBlockMeta(), smallest);
		else if (canPlaceBlockAt(loc))
			return -1;
		else
			return smallest;
	}

	private static void grow(WorldLocation loc, int test) {
		if (loc.getBlock() == Blocks.snow_layer && loc.getBlockMeta() == test || test == -1 && canPlaceBlockAt(loc))
			loc.placeBlock(new SimpleItem(Blocks.snow_layer, test + 1));
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
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, snow.stepSound.func_150496_b(),
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
		if (event.harvester != null)
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
