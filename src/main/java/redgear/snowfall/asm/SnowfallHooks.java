package redgear.snowfall.asm;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import redgear.core.api.world.ILocation;
import redgear.core.util.SimpleItem;
import redgear.core.world.Location;

public class SnowfallHooks {

	static {
		MinecraftForge.EVENT_BUS.register(new SnowfallHooks());
	}

	private SnowfallHooks() {
	}

	public static void updateTick(World world, int x, int y, int z, Random rand) {
		Location loc = new Location(x, y, z);

		int meta = loc.getBlockMeta(world);
		int k1 = meta & 7;

		if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11)
			if (meta < 1)
				world.setBlockToAir(loc.getX(), loc.getY(), loc.getZ());
			else
				loc.placeBlock(world, new SimpleItem(Block.snow.blockID, k1 - 1));
		//else

		//if (meta < 7 && world.isRaining() && rand.nextInt(8) + 1 > 7 && world.canBlockSeeTheSky(x, y, z))
		//	loc.placeBlock(new SimpleItem(Blocks.snow_layer, k1 + 1 | meta & -8));
	}

	public static boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return canPlaceBlockAt(world, new Location(x, y, z));
	}

	//Block below must be soid, with leaves being an exception. 
	public static boolean canPlaceBlockAt(World world, ILocation loc) {
		int id = loc.getBlockId(world);
		Block block = Block.blocksList[id];

		if (id == Block.snow.blockID || loc.isAir(world) || block.blockMaterial == Material.plants
				|| block.blockMaterial == Material.vine) {//This block

			loc = loc.translate(ForgeDirection.DOWN, 1);
			id = loc.getBlockId(world);
			block = Block.blocksList[id];
			return block != null && (
					world.isBlockSolidOnSide(loc.getX(), loc.getY(), loc.getZ(), ForgeDirection.UP)//Block below.
					|| block.isLeaves(world, loc.getX(), loc.getY(), loc.getZ()) 
					|| id == Block.tilledField.blockID);
		} else
			return false;
	}

	public static boolean canSnowAtBody(World world, int x, int y, int z) {
		ILocation loc = new Location(x, y, z);
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x, z);
		float f = biomegenbase.getFloatTemperature();

		if (f > 0.15F)
			return false;
		else if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10) {
			int block = loc.getBlockId(world);

			if (block == Block.snow.blockID) { //grow the snow. 
				int smallest = loc.getBlockMeta(world);

				if (smallest == 7) {
					if (Snowfall.deepSnow || Snowfall.iceAge) {
						loc = loc.translate(-1, 0, -1);

						for (int i = 0; i < 3; i++)
							for (int j = 0; j < 3; j++)
								deepSnow(world, loc.translate(i, 0, j));
					}
					return false;
				}

				if (world.rand.nextInt(smallest + 2) == 0) {
					loc = loc.translate(-1, 0, -1);

					for (int i = 0; i < 3; i++)
						for (int j = 0; j < 3; j++)
							smallest = findSmallest(world, loc.translate(i, 0, j), smallest);

					for (int i = 0; i < 3; i++)
						for (int j = 0; j < 3; j++)
							grow(world, loc.translate(i, 0, j), smallest);
				}
				return false;

			}

			return canPlaceBlockAt(world, x, y, z);
		} else
			return false;
	}

	private static void deepSnow(World world, ILocation loc) {
		if (loc.getBlockMeta(world) == 7) {
			if (Snowfall.deepSnow && !Snowfall.iceAge)
				if (!checkDeepSnow(world, loc.translate(ForgeDirection.DOWN, 2)))
					loc.placeBlock(world, new SimpleItem(Block.blockSnow));

			if (Snowfall.iceAge) {
				if (!(loc.translate(ForgeDirection.DOWN, 6).getBlockId(world) == Block.ice.blockID))
					loc.placeBlock(world, new SimpleItem(Block.blockSnow));

				loc = loc.translate(ForgeDirection.DOWN, 3);
				if (loc.getBlockId(world) == Block.blockSnow.blockID)
					loc.placeBlock(world, new SimpleItem(Block.ice));

			}
		}
	}

	private static boolean checkDeepSnow(World world, ILocation loc) {
		Material mat = loc.getBlockMaterial(world);
		return mat == Material.snow ? true : mat == Material.craftedSnow ? true : mat == Material.ice;
	}

	private static int findSmallest(World world, ILocation loc, int smallest) {
		if (loc.getBlockId(world) == Block.snow.blockID)
			return Math.min(loc.getBlockMeta(world), smallest);
		else if (canPlaceBlockAt(world, loc))
			return -1;
		else
			return smallest;
	}

	private static void grow(World world, ILocation loc, int test) {
		if (loc.getBlockId(world) == Block.snow.blockID && loc.getBlockMeta(world) == test || test == -1
				&& canPlaceBlockAt(world, loc))
			loc.placeBlock(world, new SimpleItem(Block.snow.blockID, test + 1));
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not
	 * BLOCKS
	 */
	public static int onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float par8, float par9, float par10) {
		ILocation loc = new Location(x, y, z);

		if (stack.stackSize == 0)
			return 1;

		if (!player.canPlayerEdit(x, y, z, side, stack))
			return 1;

		int target = loc.getBlockId(world);
		Block snow = Block.snow;

		if (target == snow.blockID) {
			int meta = loc.getBlockMeta(world);

			if (meta <= 6) {
				if (world.checkNoEntityCollision(snow.getCollisionBoundingBoxFromPool(world, x, y, z))
						&& world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2)) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, snow.stepSound.getBreakSound(),
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
	@ForgeSubscribe
	public void snowShovelHook(HarvestDropsEvent event) {
		if (event.harvester != null)
			if (event.block == Block.blockSnow || event.block == Block.snow) {
				ItemStack heldItem = event.harvester.getHeldItem();

				if (heldItem != null && heldItem.getItem() instanceof ISnowShovel) {
					event.drops.clear();

					if (event.block == Block.snow)
						event.drops.add(new ItemStack(Block.snow, event.blockMetadata + 1, 0));
					else
						event.drops.add(new ItemStack(Block.blockSnow, 1, 0));
				}
			}
	}

	public interface ISnowShovel {

	}
}
