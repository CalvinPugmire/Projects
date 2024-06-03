package net.calvinpugmire.agiagentmod.entity.client;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation AGENT_LAYER = new ModelLayerLocation(
            new ResourceLocation(AGIAgentMod.MOD_ID, "agent_layer"), "main");
}
