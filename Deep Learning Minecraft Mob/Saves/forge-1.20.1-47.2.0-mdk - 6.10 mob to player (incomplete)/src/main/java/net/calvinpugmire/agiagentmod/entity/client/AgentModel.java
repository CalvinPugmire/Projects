package net.calvinpugmire.agiagentmod.entity.client;

import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;

public class AgentModel<T extends Player> extends PlayerModel<T> {
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