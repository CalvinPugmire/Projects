package net.calvinpugmire.agiagentmod.entity.custom;

import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AgentEntityAi {
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for inventory.

    private static void putInInventory(AgentEntity pAgent, ItemStack pStack) {
        pAgent.addToInventory(pStack);
    }

    private static void holdInMainhand(AgentEntity pAgent, ItemStack pStack) {
        pAgent.holdInMainHand(pStack);
    }

    private static boolean isHoldingItemInMainHand(AgentEntity pAgent) {
        return !pAgent.getMainHandItem().isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for picking up items.

    protected static boolean wantsToPickup(AgentEntity pAgent, ItemStack pStack) {
        boolean flag1 = pAgent.canAddToInventory(pStack);
        boolean flag2 = isHoldingItemInMainHand(pAgent);
        return flag1 || !flag2;
    }

    protected static void pickUpItem(AgentEntity pAgent, ItemEntity pItemEntity) {
        ItemStack itemstack;

        pAgent.take(pItemEntity, pItemEntity.getItem().getCount());
        itemstack = pItemEntity.getItem();
        pItemEntity.discard();

        if (isHoldingItemInMainHand(pAgent)) {
            putInInventory(pAgent, itemstack);
        } else {
            holdInMainhand(pAgent, itemstack);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for actions.

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
