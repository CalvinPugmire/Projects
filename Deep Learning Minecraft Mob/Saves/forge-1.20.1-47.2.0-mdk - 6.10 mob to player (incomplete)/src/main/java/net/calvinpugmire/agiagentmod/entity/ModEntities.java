package net.calvinpugmire.agiagentmod.entity;

import net.calvinpugmire.agiagentmod.AGIAgentMod;
import net.calvinpugmire.agiagentmod.entity.custom.AgentEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AGIAgentMod.MOD_ID);

    public static final RegistryObject<EntityType<AgentEntity>> AGENT =
            ENTITY_TYPES.register("agent", () -> EntityType.Builder.of(AgentEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F).clientTrackingRange(32).updateInterval(2)
                    .build("agent"));

    /*public static final RegistryObject<EntityType<AgentEntity>> AGENT =
            ENTITY_TYPES.register("agent", () -> EntityType.Builder.<AgentEntity>createNothing(MobCategory.MISC)
                    .sized(0.6F, 1.8F).clientTrackingRange(32).updateInterval(2)
                    .build("agent"));*/

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
