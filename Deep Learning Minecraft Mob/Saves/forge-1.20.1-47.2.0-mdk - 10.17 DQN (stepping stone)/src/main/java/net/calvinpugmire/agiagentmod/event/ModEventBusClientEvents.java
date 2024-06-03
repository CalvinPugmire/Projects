package net.calvinpugmire.agiagentmod.event;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.client.AgentModel;
import net.calvinpugmire.agiagentmod.entity.client.ModModelLayers;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AGIAgentMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.AGENT_LAYER, AgentModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.AGENT_INNER_ARMOR, AgentModel::createInnerArmorLayer);
        event.registerLayerDefinition(ModModelLayers.AGENT_OUTER_ARMOR, AgentModel::createOuterArmorLayer);
    }
}
