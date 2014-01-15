package redgear.snowfall.asm;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumHelper;
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

@Mod(modid = "RedGear|Snowfall", name = "Snowfall", version = "@ModVersion@", dependencies = "required-after:RedGear|Core;")
public class Snowfall extends ModUtils {

	@Instance("RedGear|Snowfall")
	public static ModUtils inst;
	public static Item itemToolSnowshovelWood;
	public static Item itemToolSnowshovelStone;
	public static Item itemToolSnowshovelBronze;
	public static Item itemToolSnowshovelIron;
	public static Item itemToolSnowshovelSteel;
	public static Item itemToolSnowshovelDiamond;
	public static Item itemToolSnowshovelGold;

	public Snowfall() {
		super(0, 23300);
	}

	@Override
	public void PreInit(FMLPreInitializationEvent event) {

		if (getBoolean("snowShovels")) {
			EnumToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("Bronze", 2, 131, 5.0F, 2, 18);
			EnumToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("Steel", 2, 500, 7.0F, 4, 10);
			itemToolSnowshovelWood = new ItemSnowShovel(getItemId("snowShovelWood"), EnumToolMaterial.WOOD)
					.setUnlocalizedName("snowShovelWood");
			
			itemToolSnowshovelStone = new ItemSnowShovel(getItemId("snowShovelStone"), EnumToolMaterial.STONE)
					.setUnlocalizedName("snowShovelStone");
			itemToolSnowshovelBronze = new ItemSnowShovel(getItemId("snowShovelBronze"), toolMaterialBronze)
					.setUnlocalizedName("snowShovelBronze");
			itemToolSnowshovelIron = new ItemSnowShovel(getItemId("snowShovelIron"), EnumToolMaterial.IRON)
					.setUnlocalizedName("snowShovelIron");
			itemToolSnowshovelSteel = new ItemSnowShovel(getItemId("snowShovelSteel"), toolMaterialSteel)
					.setUnlocalizedName("snowShovelSteel");
			itemToolSnowshovelDiamond = new ItemSnowShovel(getItemId("snowShovelDiamond"), EnumToolMaterial.EMERALD)
					.setUnlocalizedName("snowShovelDiamond");
			itemToolSnowshovelGold = new ItemSnowShovel(getItemId("snowShovelGold"), EnumToolMaterial.GOLD)
					.setUnlocalizedName("snowShovelGold");
			
			
			GameRegistry.registerItem(itemToolSnowshovelStone, "snowShovelWood", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelWood, "snowShovelStone", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelBronze, "snowShovelBronze", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelIron, "snowShovelIron", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelSteel, "snowShovelSteel", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelDiamond, "snowShovelDiamond", "RedGear|Snowfall");
			GameRegistry.registerItem(itemToolSnowshovelGold, "snowShovelGold", "RedGear|Snowfall");
			
			

			if (getBoolean("altShovelRecipe", false)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelWood, new Object[] {"PPP", "PXP", " S ",
						'P', "plankWood", 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelStone, new Object[] {"PPP", "PXP", " S ",
						'P', Block.cobblestone, 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelBronze, new Object[] {"PPP", "PXP", " S ",
						'P', "ingotBronze", 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelIron, new Object[] {"PPP", "PXP", " S ",
						'P', Item.ingotIron, 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelSteel, new Object[] {"PPP", "PXP", " S ",
						'P', "ingotSteel", 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelDiamond, new Object[] {"PPP", "PXP",
						" S ", 'P', Item.diamond, 'S', Item.stick, 'X', Item.shovelWood }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelGold, new Object[] {"PPP", "PXP", " S ",
						'P', Item.ingotGold, 'S', Item.stick, 'X', Item.shovelWood }));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelWood, new Object[] {"PPP", "PSP", " S ",
						'P', "plankWood", 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelStone, new Object[] {"PPP", "PSP", " S ",
						'P', Block.cobblestone, 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelBronze, new Object[] {"PPP", "PSP", " S ",
						'P', "ingotBronze", 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelIron, new Object[] {"PPP", "PSP", " S ",
						'P', Item.ingotIron, 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelSteel, new Object[] {"PPP", "PSP", " S ",
						'P', "ingotSteel", 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelDiamond, new Object[] {"PPP", "PSP",
						" S ", 'P', Item.diamond, 'S', Item.stick }));
				GameRegistry.addRecipe(new ShapedOreRecipe(itemToolSnowshovelGold, new Object[] {"PPP", "PSP", " S ",
						'P', Item.ingotGold, 'S', Item.stick }));
			}
		}

		if (getBoolean("snowGolemRecipe") && Loader.isModLoaded("NotEnoughItems")) //NEI makes the monster placer usable. It's worthless without it
			GameRegistry.addRecipe(new ItemStack(Item.monsterPlacer, 1, 97), new Object[] {" J ", "SBS", " B ", 'J',
					Block.pumpkinLantern, 'S', Item.stick, 'B', Block.blockSnow });

		if (getBoolean("snowBlockFromLayerRecipe"))
			GameRegistry.addRecipe(new ItemStack(Block.blockSnow), new Object[] {"LL", "LL", 'L', Block.snow });

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
