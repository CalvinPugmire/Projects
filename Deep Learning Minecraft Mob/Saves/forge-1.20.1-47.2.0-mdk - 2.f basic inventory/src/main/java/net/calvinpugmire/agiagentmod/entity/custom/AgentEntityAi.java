package net.calvinpugmire.agiagentmod.entity.custom;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AgentEntityAi {
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for inventory.

    protected static void pickUpItem(AgentEntity pAgent, ItemEntity pItemEntity) {
        ItemStack itemstack;

        itemstack = pItemEntity.getItem();

        if (pAgent.getMainHandItem().isEmpty()) {
            pAgent.take(pItemEntity, pItemEntity.getItem().getCount());
            pItemEntity.discard();
            pAgent.holdInMainHand(itemstack);
        }

        else if (pAgent.canAddToInventory(itemstack)) {
            pAgent.take(pItemEntity, pItemEntity.getItem().getCount());
            pItemEntity.discard();
            pAgent.addToInventory(itemstack);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
