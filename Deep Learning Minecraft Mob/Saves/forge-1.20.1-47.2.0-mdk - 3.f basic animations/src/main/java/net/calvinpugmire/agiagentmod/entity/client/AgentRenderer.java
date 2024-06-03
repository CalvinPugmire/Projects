package net.calvinpugmire.agiagentmod.entity.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AgentRenderer extends HumanoidMobRenderer<Mob, AgentModel<Mob>> {
    public AgentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, createModel(pContext.getModelSet(), ModModelLayers.AGENT_LAYER), 0.5f, 1.0F, 1.0F, 1.0F);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidArmorModel(pContext.bakeLayer(ModModelLayers.AGENT_INNER_ARMOR)),
                new HumanoidArmorModel(pContext.bakeLayer(ModModelLayers.AGENT_OUTER_ARMOR)),
                pContext.getModelManager()));
    }

    private static AgentModel<Mob> createModel(EntityModelSet pModelSet, ModelLayerLocation pLayer) {
        return new AgentModel<>(pModelSet.bakeLayer(pLayer));
    }

    public ResourceLocation getTextureLocation(Mob pEntity) {
        return new ResourceLocation(AGIAgentMod.MOD_ID, "textures/entity/agentmodel.png");
    }
}
