package redgear.snowfall.asm;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import redgear.core.mod.ModUtils;
import redgear.snowfall.item.ItemSnowShovel;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "redgear_snowfall", name = "Snowfall", version = "@ModVersion@", dependencies = "required-after:redgear_core;")
public class Snowfall extends ModUtils {

	@Instance("redgear_snowfall")
	public static ModUtils inst;
	public static boolean deepSnow;
	public static boolean iceAge;
	
	public static Item itemToolSnowshovelWood;
	public static Item itemToolSnowshovelStone;
	public static Item itemToolSnowshovelBronze;
	public static Item itemToolSnowshovelIron;
	public static Item itemToolSnowshovelSteel;
	public static Item itemToolSnowshovelDiamond;
	public static Item itemToolSnowshovelGold;

	@Override
	public void PreInit(FMLPreInitializationEvent event) {
		new SnowfallHooks();
		
		deepSnow = getBoolean("DeepSnow", false );
		iceAge = getBoolean("IceAgeMode", false);

		if (getBoolean("snowShovels")) {
			ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("Bronze", 2, 131, 5.0F, 2, 18);
			ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("Steel", 2, 500, 7.0F, 4, 10);
			itemToolSnowshovelWood = new ItemSnowShovel(ToolMaterial.WOOD).setUnlocalizedName("snowShovelWood");

			itemToolSnowshovelStone = new ItemSnowShovel(ToolMaterial.STONE).setUnlocalizedName("snowShovelStone");
			itemToolSnowshovelBronze = new ItemSnowShovel(toolMaterialBronze).setUnlocalizedName("snowShovelBronze");
			itemToolSnowshovelIron = new ItemSnowShovel(ToolMaterial.IRON).setUnlocalizedName("snowShovelIron");
			itemToolSnowshovelSteel = new ItemSnowShovel(toolMaterialSteel).setUnlocalizedName("snowShovelSteel");
			itemToolSnowshovelDiamond = new ItemSnowShovel(ToolMaterial.EMERALD)
					.setUnlocalizedName("snowShovelDiamond");
			itemToolSnowshovelGold = new ItemSnowShovel(ToolMaterial.GOLD).setUnlocalizedName("snowShovelGold");

			GameRegistry.registerItem(itemToolSnowshovelStone, "snowShovelWood", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelWood, "snowShovelStone", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelBronze, "snowShovelBronze", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelIron, "snowShovelIron", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelSteel, "snowShovelSteel", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelDiamond, "snowShovelDiamond", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelGold, "snowShovelGold", "RedGear|Snowfall");

			if (getBoolean("altShovelRecipe", false)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelWood, new Object[] {"PPP", "PXP", " S ",
						'P', "plankWood", 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelStone, new Object[] {"PPP", "PXP", " S ",
						'P', Blocks.cobblestone, 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelBronze, new Object[] {"PPP", "PXP", " S ",
						'P', "ingotBronze", 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelIron, new Object[] {"PPP", "PXP", " S ",
						'P', Items.iron_ingot, 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelSteel, new Object[] {"PPP", "PXP", " S ",
						'P', "ingotSteel", 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelDiamond, new Object[] {"PPP", "PXP",
						" S ", 'P', Items.diamond, 'S', Items.stick, 'X', Items.wooden_shovel }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelGold, new Object[] {"PPP", "PXP", " S ",
						'P', Items.gold_ingot, 'S', Items.stick, 'X', Items.wooden_shovel }));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelWood, new Object[] {"PPP", "PSP", " S ",
						'P', "plankWood", 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelStone, new Object[] {"PPP", "PSP", " S ",
						'P', Blocks.cobblestone, 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelBronze, new Object[] {"PPP", "PSP", " S ",
						'P', "ingotBronze", 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelIron, new Object[] {"PPP", "PSP", " S ",
						'P', Items.iron_ingot, 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelSteel, new Object[] {"PPP", "PSP", " S ",
						'P', "ingotSteel", 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelDiamond, new Object[] {"PPP", "PSP",
						" S ", 'P', Items.diamond, 'S', Items.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelGold, new Object[] {"PPP", "PSP", " S ",
						'P', Items.gold_ingot, 'S', Items.stick }));
			}
		}

		if (getBoolean("snowGolemRecipe") && Loader.isModLoaded("NotEnoughItems")) //NEI makes the monster placer usable. It's worthless without it
			GameRegistry.addRecipe(new ItemStack(Items.spawn_egg, 1, 97), new Object[] {" J ", "SBS", " B ", 'J',
					Blocks.lit_pumpkin, 'S', Items.stick, 'B', Blocks.snow });

		if (getBoolean("snowBlockFromLayerRecipe"))
			GameRegistry.addRecipe(new ItemStack(Blocks.snow), new Object[] {"LL", "LL", 'L', Blocks.snow_layer });

	}

	@Override
	public void Init(FMLInitializationEvent event) {

	}

	@Override
	protected void PostInit(FMLPostInitializationEvent event) {

	}

	@Override
	@EventHandler
	public void PreInitialization(FMLPreInitializationEvent event) {
		super.PreInitialization(event);
	}

	@Override
	@EventHandler
	public void Initialization(FMLInitializationEvent event) {
		super.Initialization(event);
	}

	@Override
	@EventHandler
	public void PostInitialization(FMLPostInitializationEvent event) {
		super.PostInitialization(event);
	}
}
