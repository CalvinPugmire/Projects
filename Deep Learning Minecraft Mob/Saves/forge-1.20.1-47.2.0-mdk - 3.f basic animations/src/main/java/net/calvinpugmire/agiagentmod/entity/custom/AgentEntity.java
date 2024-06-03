package net.calvinpugmire.agiagentmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AgentEntity extends Mob implements InventoryCarrier {
    //Variables.
    private final SimpleContainer inventory = new SimpleContainer(10);

    //Definition.
    public AgentEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Animation logic.

    /*@Override
    public void tick () {
        super.tick();
    }*/

    /*@Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }*/

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Main logic (attributes and goals).

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 1f)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.FOLLOW_RANGE);
    }

    @Override
    protected void registerGoals() {
        //TODO
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for inventory.

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.writeInventoryToTag(pCompound);
    }

    //(abstract) Protected helper method to read subclass entity data from NBT.
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readInventoryFromTag(pCompound);
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
    }

    protected ItemStack addToInventory(ItemStack pStack) {
        return this.inventory.addItem(pStack);
    }

    protected boolean canAddToInventory(ItemStack pStack) {
        return this.inventory.canAddItem(pStack);
    }


    protected void holdInMainHand(ItemStack pStack) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, pStack);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for picking up items.

    public boolean wantsToPickUp(ItemStack pStack) {
        return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.canPickUpLoot() && AgentEntityAi.wantsToPickup(this, pStack);
    }

    //Tests if this entity should pick up an item.
    protected void pickUpItem(ItemEntity pItemEntity) {
        this.onItemPickup(pItemEntity);
        AgentEntityAi.pickUpItem(this, pItemEntity);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
