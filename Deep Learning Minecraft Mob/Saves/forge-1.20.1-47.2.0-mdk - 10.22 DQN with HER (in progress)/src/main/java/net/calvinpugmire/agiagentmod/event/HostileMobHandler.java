package net.calvinpugmire.agiagentmod.event;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AGIAgentMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HostileMobHandler {
    @SubscribeEvent
    public void spawnEvent(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(monster, AgentEntity.class, true));
        }
    }
}
