package net.calvinpugmire.agiagentmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AgentRenderer extends MobRenderer<AgentEntity, AgentModel<AgentEntity>> {
    public AgentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AgentModel<>(pContext.bakeLayer((ModModelLayers.AGENT_LAYER))), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(AgentEntity pEntity) {
        return new ResourceLocation(AGIAgentMod.MOD_ID, "textures/entity/agent.png");
    }

    @Override
    public void render(AgentEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
