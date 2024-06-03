package net.calvinpugmire.agiagentmod.entity.client;
// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.calvinpugmire.agiagentmod.entity.animations.ModAnimationDefinitions;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.*;

public class AgentModel<T extends Mob> extends PlayerModel<T> {
	public static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
	public static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);

	public AgentModel(ModelPart pRoot) {
		super(pRoot, false);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createInnerArmorLayer() {
		return LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32);
	}

	public static LayerDefinition createOuterArmorLayer() {
		return LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32);
	}
}