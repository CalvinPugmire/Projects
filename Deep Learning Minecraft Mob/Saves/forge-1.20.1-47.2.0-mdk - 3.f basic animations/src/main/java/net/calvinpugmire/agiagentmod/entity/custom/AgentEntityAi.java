package net.calvinpugmire.agiagentmod.entity.custom;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class AgentEntityAi {
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for inventory.

    private static void holdInMainhand(AgentEntity pAgent, ItemStack pStack) {
        pAgent.holdInMainHand(pStack);
    }

    private static void putInInventory(AgentEntity pAgent, ItemStack pStack) {
        ItemStack itemstack = pAgent.addToInventory(pStack);
    }

    private static boolean isHoldingItemInMainHand(AgentEntity pAgent) {
        return !pAgent.getMainHandItem().isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for picking up items.

    protected static void pickUpItem(AgentEntity pAgent, ItemEntity pItemEntity) {
        ItemStack itemstack;

        pAgent.take(pItemEntity, pItemEntity.getItem().getCount());
        itemstack = pItemEntity.getItem();
        pItemEntity.discard();

        //boolean flag = !pAgent.equipItemIfPossible(itemstack).equals(ItemStack.EMPTY);

        if (isHoldingItemInMainHand(pAgent)) {
            putInInventory(pAgent, itemstack);
        } else {
            holdInMainhand(pAgent, itemstack);
        }
    }

    protected static boolean wantsToPickup(AgentEntity pAgent, ItemStack pStack) {
        boolean flag1 = pAgent.canAddToInventory(pStack);
        boolean flag2 = isHoldingItemInMainHand(pAgent);
        return flag1 || !flag2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
