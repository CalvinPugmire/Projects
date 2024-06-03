package net.calvinpugmire.agiagentmod.entity.client;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AgentRenderer extends LivingEntityRenderer<Player, AgentModel<Player>> {
    public AgentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, createModel(pContext.getModelSet(), ModModelLayers.AGENT_LAYER), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidArmorModel(pContext.bakeLayer(ModModelLayers.AGENT_INNER_ARMOR)),
                new HumanoidArmorModel(pContext.bakeLayer(ModModelLayers.AGENT_OUTER_ARMOR)),
                pContext.getModelManager()));
    }

    private static AgentModel<Player> createModel(EntityModelSet pModelSet, ModelLayerLocation pLayer) {
        return new AgentModel<>(pModelSet.bakeLayer(pLayer));
    }

    public ResourceLocation getTextureLocation(Player pEntity) {
        return new ResourceLocation(AGIAgentMod.MOD_ID, "textures/entity/agentmodel.png");
    }
}
