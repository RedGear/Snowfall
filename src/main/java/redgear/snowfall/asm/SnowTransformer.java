package redgear.snowfall.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import redgear.core.asm.RedGearCore;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class SnowTransformer implements IClassTransformer, IFMLLoadingPlugin {
	private static String worldName = "Lnet/minecraft/world/World;";
	private static String playerName = "Lnet/minecraft/entity/player/EntityPlayer;";
	private static String itemStackName = "Lnet/minecraft/item/ItemStack;";
	private static String snowfallHooks = "redgear/snowfall/asm/SnowfallHooks";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (transformedName.equals("net.minecraft.world.World") && RedGearCore.util.getBoolean("SnowOnIce")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			InsnList canSnowAtBody = new InsnList();
			canSnowAtBody.add(new VarInsnNode(ALOAD, 0));
			canSnowAtBody.add(new VarInsnNode(ILOAD, 1));
			canSnowAtBody.add(new VarInsnNode(ILOAD, 2));
			canSnowAtBody.add(new VarInsnNode(ILOAD, 3));
			canSnowAtBody.add(new VarInsnNode(ILOAD, 4));
			canSnowAtBody.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "canSnowAtBody", "(" + worldName
					+ "IIIZ)Z"));
			canSnowAtBody.add(new InsnNode(IRETURN));

			for (MethodNode mn : node.methods)
				if (mn.name.equals("canSnowAtBody"))
					mn.instructions = canSnowAtBody;

			ClassWriter writer = new ClassWriter(0);
			node.accept(writer);
			return writer.toByteArray();
		}

		if (transformedName.equals("net.minecraft.item.ItemSnow") && RedGearCore.util.getBoolean("SnowLayerStackFix")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			InsnList onItemUse = new InsnList();
			LabelNode skip29 = new LabelNode();
			LabelNode skip38 = new LabelNode();
			onItemUse.add(new VarInsnNode(ALOAD, 1));
			onItemUse.add(new VarInsnNode(ALOAD, 2));
			onItemUse.add(new VarInsnNode(ALOAD, 3));
			onItemUse.add(new VarInsnNode(ILOAD, 4));
			onItemUse.add(new VarInsnNode(ILOAD, 5));
			onItemUse.add(new VarInsnNode(ILOAD, 6));
			onItemUse.add(new VarInsnNode(ILOAD, 7));
			onItemUse.add(new VarInsnNode(FLOAD, 8));
			onItemUse.add(new VarInsnNode(FLOAD, 9));
			onItemUse.add(new VarInsnNode(FLOAD, 10));
			onItemUse.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "onItemUse", "(" + itemStackName + playerName
					+ worldName + "IIIIFFF)I"));
			onItemUse.add(new VarInsnNode(ISTORE, 11));
			onItemUse.add(new VarInsnNode(ILOAD, 11));
			onItemUse.add(new JumpInsnNode(IFNE, skip29));
			onItemUse.add(new InsnNode(ICONST_1));
			onItemUse.add(new InsnNode(IRETURN));
			onItemUse.add(skip29);
			onItemUse.add(new VarInsnNode(ILOAD, 11));
			onItemUse.add(new InsnNode(ICONST_2));
			onItemUse.add(new JumpInsnNode(IF_ICMPNE, skip38));
			onItemUse.add(new IincInsnNode(5, 1));
			onItemUse.add(skip38);
			onItemUse.add(new VarInsnNode(ALOAD, 0));
			onItemUse.add(new VarInsnNode(ALOAD, 1));
			onItemUse.add(new VarInsnNode(ALOAD, 2));
			onItemUse.add(new VarInsnNode(ALOAD, 3));
			onItemUse.add(new VarInsnNode(ILOAD, 4));
			onItemUse.add(new VarInsnNode(ILOAD, 5));
			onItemUse.add(new VarInsnNode(ILOAD, 6));
			onItemUse.add(new VarInsnNode(ILOAD, 7));
			onItemUse.add(new VarInsnNode(FLOAD, 8));
			onItemUse.add(new VarInsnNode(FLOAD, 9));
			onItemUse.add(new VarInsnNode(FLOAD, 10));
			onItemUse.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "func_77648_a", "("
					+ itemStackName + playerName + worldName + "IIIIFFF)Z"));
			onItemUse.add(new InsnNode(IRETURN));

			for (MethodNode method : node.methods)
				if ("func_77648_a".equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name,
						method.desc)))
					method.instructions = onItemUse;

			ClassWriter writer = new ClassWriter(0);
			node.accept(writer);
			return writer.toByteArray();
		}

		if (transformedName.equals("net.minecraft.block.BlockSnow")
				&& (RedGearCore.util.getBoolean("SnowGrowthAndDecay")
						|| RedGearCore.util.getBoolean("SnowPlaceOnSolidSide") || RedGearCore.util
							.getBoolean("SnowfallShovelHook"))) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			InsnList updateTick = new InsnList();
			updateTick.add(new VarInsnNode(ALOAD, 1));
			updateTick.add(new VarInsnNode(ILOAD, 2));
			updateTick.add(new VarInsnNode(ILOAD, 3));
			updateTick.add(new VarInsnNode(ILOAD, 4));
			updateTick.add(new VarInsnNode(ALOAD, 5));
			updateTick.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "updateTick", "(" + worldName
					+ "IIILjava/util/Random;)V")); // call the updateTick method from the SnowTransformer class
			updateTick.add(new InsnNode(RETURN));
			InsnList canPlaceBlockAt = new InsnList();
			canPlaceBlockAt.add(new VarInsnNode(ALOAD, 1));
			canPlaceBlockAt.add(new VarInsnNode(ILOAD, 2));
			canPlaceBlockAt.add(new VarInsnNode(ILOAD, 3));
			canPlaceBlockAt.add(new VarInsnNode(ILOAD, 4));
			canPlaceBlockAt.add(new MethodInsnNode(INVOKESTATIC, snowfallHooks, "canPlaceBlockAt", "(" + worldName
					+ "III)Z"));
			canPlaceBlockAt.add(new InsnNode(IRETURN));

			for (MethodNode method : node.methods) {
				String mappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
				if ("func_149674_a".equals(mappedName) && RedGearCore.util.getBoolean("SnowGrowthAndDecay"))//func_71847_b
					method.instructions = updateTick;//func_149674_a

				if ("func_149742_c".equals(mappedName) && RedGearCore.util.getBoolean("SnowPlaceOnSolidSide"))//func_71930_b
					method.instructions = canPlaceBlockAt;
			}

			ClassWriter writer = new ClassWriter(0);
			node.accept(writer);
			return writer.toByteArray();
		}

		return bytes;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"redgear.snowfall.asm.SnowTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> arg0) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
