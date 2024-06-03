package net.calvinpugmire.agiagentmod.entity.client;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation AGENT_LAYER = register("agent_layer");
    public static final ModelLayerLocation AGENT_INNER_ARMOR = registerInnerArmor("agent_layer");
    public static final ModelLayerLocation AGENT_OUTER_ARMOR = registerOuterArmor("agent_layer");

    private static ModelLayerLocation register(String pPath) {
        return register(pPath, "main");
    }

    private static ModelLayerLocation register(String pPath, String pModel) {
        return createLocation(pPath, pModel);
    }

    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(new ResourceLocation(AGIAgentMod.MOD_ID, pPath), pModel);
    }

    private static ModelLayerLocation registerInnerArmor(String pPath) {
        return register(pPath, "inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String pPath) {
        return register(pPath, "outer_armor");
    }
}
