package net.calvinpugmire.agiagentmod.entity.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.GameMasterBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.GameData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AgentEntity extends Mob implements InventoryCarrier {
    //Variables.
    private final SimpleContainer inventory = new SimpleContainer(10);
    private String oldSightBlocker = "";
    private List<String> oldHearInfo = null;
    private List<Float> oldRewardInfo = null;
    private Entity oldEntitySightBlocker;
    private Pair<Block, BlockPos> oldBlockSightBlocker;
    private BlockPos oldBlockPlaceable;
    private double breakTime = 0.0;
    private double placeTime = 0.0;
    private double switchTime = 0.0;

    //Definition.
    public AgentEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
        this.moveControl = new AgentEntity.AgentMoveControl(this);
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 1.79F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        //this.entityData.define(DATA_POSE, this.getPose());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Main logic (update steps).
    public void aiStep () {
        this.updateSwingTime();
        super.aiStep();
    }

    public void tick () {
        super.tick();
        sendInput();
        String[] sample = new String[]{"forward", "left", "", "hit", ""/*String.valueOf(random.nextInt(10))*/};
        receiveAction(sample);
    }

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

    protected void holdInOffHand(ItemStack pStack) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, pStack);
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

    //Logic for inputs.

    private void sendInput() {
        List<String> newSightInfo = sight();
        List<String> newHearInfo = hear();
        List<Float> newRewardInfo = reward();
    }

    private List<String> sight() {
        boolean invisible = true;
        double lookDist = 0.0D;
        List<String> newSightInfo = new ArrayList<>();

        while (invisible) {
            //Get next distance-from-agent spot in agent's line-of-sight.
            Vec3 rawLookSpot = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
            Vec3 extLookSpot = rawLookSpot.scale(lookDist);
            Vec3 lookSpot = this.getEyePosition().add(extLookSpot);
            BlockPos blockSpot = new BlockPos(new Vec3i((int)lookSpot.x-1, (int)lookSpot.y, (int)lookSpot.z));

            //Get block and/or entity in said spot.
            Block block = this.level().getBlockState(blockSpot).getBlock();
            List<Entity> entities = this.level().getEntities(null, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(blockSpot)));

            if (lookDist > 100.0D) {
                //If out-of-range.
                oldEntitySightBlocker = null;
                oldBlockSightBlocker = null;
                oldBlockPlaceable = null;
                ///
                lookDist -= 1;
                invisible = false;
            } else if (!entities.isEmpty()) {
                //If sight blocked by entity.
                oldBlockSightBlocker = null;
                oldBlockPlaceable = null;
                ///
                Entity entTop = entities.get(0);
                String entName = entTop.getName().getString()/*getEncodeId()*/;
                String nextEntityInfo = entName + " :: " + blockSpot;
                newSightInfo.add(nextEntityInfo);
                ///
                if (!entName.equals(this.getName().getString()/*getEncodeId()*/)) {
                    oldEntitySightBlocker = entTop;
                    ///
                    invisible = false;
                } else {
                    lookDist += 1.0D;
                }
            } else {
                //If sight blocked by block.
                oldEntitySightBlocker = null;
                ///
                String blockName = block.getDescriptionId();
                String nextBlockInfo = blockName + " :: " + blockSpot;
                newSightInfo.add(nextBlockInfo);
                ///
                if (!blockName.equals("block.minecraft.air")) {
                    oldBlockSightBlocker = new Pair<>(block, blockSpot);
                    ///
                    invisible = false;
                } else {
                    oldBlockPlaceable = blockSpot;
                    ///
                    lookDist += 1.0D;
                }
            }
        }

        //Test to see what is blocking sight (what the agent sees).
        String sightBlocker = newSightInfo.get((int)lookDist);
        if (!sightBlocker.equals(oldSightBlocker)) {
            oldSightBlocker = sightBlocker;
            System.out.println(sightBlocker);
        }

        return newSightInfo;
    }

    private List<String> hear() {
        //Get all entities within agent's range-of-hearing that are not silent.
        AABB aabb = this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<Entity> hearList = this.level().getEntitiesOfClass(Entity.class, aabb, (entity) -> {
            return (entity != this) && !entity.isSilent();
        });
        hearList.sort(Comparator.comparingDouble(this::distanceToSqr));
        //
        List<String> newHearInfo = new ArrayList<>();
        for (Entity entity : hearList) {
            String nextEntityInfo = entity.getName().getString() + " :: " + entity.blockPosition();
            newHearInfo.add(nextEntityInfo);
        }

        //Test to see what the agent hears.
        if (!newHearInfo.equals(oldHearInfo)) {
            oldHearInfo = newHearInfo;
            System.out.println(newHearInfo);
        }

        return newHearInfo;
    }

    private List<Float> reward() {
        List<Float> newRewardInfo = new ArrayList<>();

        newRewardInfo.add(this.getHealth());
        newRewardInfo.add(this.getArmorCoverPercentage());

        if (!newRewardInfo.equals(oldRewardInfo)) {
            oldRewardInfo = newRewardInfo;
            System.out.println(newRewardInfo);
        }

        return newRewardInfo;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //Logic for actions.

    private void receiveAction(String[] action) {
        switch (action[0]) {
            case "forward" -> move(0.2F, 0.0F, 0.0F);
            case "backward" -> move(-0.2F, 0.0F, 0.0F);
            case "left" -> move(0.0F, 0.0F, 0.2F);
            case "right" -> move(0.0F, 0.0F, -0.2F);
        }

        switch (action[1]) {
            case "up" -> look(-3.0F, 0.0F);
            case "down" -> look(3.0F, 0.0F);
            case "left" -> look(0.0F, -3.0F);
            case "right" -> look(0.0F, 3.0F);
        }

        if (action[2].equals("jump")) {
            this.jumpControl.jump();
        }

        if (action[3].equals("hit")) {
            this.swing(this.getUsedItemHand());
            if (oldEntitySightBlocker != null && oldEntitySightBlocker.isAttackable() &&
                    this.position().distanceTo(oldEntitySightBlocker.position()) < 2.5) {
                breakTime = 0.0;
                ///
                this.doHurtTarget(oldEntitySightBlocker);
            } else if (oldBlockSightBlocker != null &&
                    this.position().distanceTo(oldBlockSightBlocker.getSecond().getCenter()) < 3.5) {
                doHurtBlock(oldBlockSightBlocker.getFirst(), oldBlockSightBlocker.getSecond());
            } else {
                breakTime = 0.0;
            }
        } else if (action[3].equals("use")) {
            breakTime = 0.0;
            ///
            String desc = this.getMainHandItem().getDescriptionId();
            if (!desc.contains("block.minecraft.air")) {
                if (this.getMainHandItem().isEdible()) {
                    this.eat(this.level(), this.getMainHandItem());
                    this.setHealth(this.getHealth()+1);
                } else if (desc.contains("block") && oldBlockPlaceable != null &&
                        this.position().distanceTo(oldBlockPlaceable.getCenter()) < 3.5) {
                    placeBlock(oldBlockPlaceable);
                }
            }
        } else {
            breakTime = 0.0;
        }

        if (!action[4].isEmpty()) {
            int invSlot = Integer.parseInt(action[4]);
            if (invSlot >= 0 && invSlot <= 9 && switchTime > 1) {
                ItemStack dummy = this.getMainHandItem();
                this.holdInMainHand(inventory.getItem(invSlot));
                inventory.setItem(invSlot, dummy);
                switchTime = 0;
            } else {
                switchTime += 0.1;
            }
        }
    }

    private void doHurtBlock(Block block, BlockPos blockPos) {
        if (breakTime >= block.defaultDestroyTime() && block.defaultDestroyTime() >= 0.0) {
            breakTime = block.defaultDestroyTime();
        } else {
            breakTime += 0.1;
        }

        if (breakTime == block.defaultDestroyTime()) {
            if (this.getMainHandItem().isCorrectToolForDrops(block.defaultBlockState())
                    || !block.defaultBlockState().requiresCorrectToolForDrops()) {
                this.level().destroyBlock(blockPos, true);
                this.level().removeBlock(blockPos, false);
            } else {
                this.level().destroyBlock(blockPos, false);
                this.level().removeBlock(blockPos, false);
            }
            breakTime = 0.0;
        }
    }

    private void placeBlock(BlockPos blockPos) {
        if (placeTime > 1) {
            this.swing(this.getUsedItemHand());
            Block newBlock = Block.byItem(this.getMainHandItem().getItem());
            this.level().setBlockAndUpdate(blockPos, newBlock.defaultBlockState());
            this.getMainHandItem().shrink(1);
            placeTime = 0;
        } else {
            placeTime += 0.1;
        }
    }

    private void move(float relX, float relY, float relZ) {
        this.getMoveControl().strafe(relX, relZ);
        this.setYBodyRot(Mth.rotateIfNecessary(this.getVisualRotationYInDegrees(), this.yHeadRot, getMaxHeadYRot()-1));
        this.setYRot(this.getVisualRotationYInDegrees()/*this.yBodyRot*/);
    }

    static class AgentMoveControl extends MoveControl {
        private final AgentEntity agent;

        public AgentMoveControl(AgentEntity pAgent) {
            super(pAgent);
            this.agent = pAgent;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.STRAFE) {
                float f = (float)this.agent.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float)this.speedModifier * f;
                this.agent.setSpeed(f1);
                this.agent.setZza(this.strafeForwards);
                this.agent.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            } else if (this.operation == MoveControl.Operation.JUMPING) {
                this.agent.setSpeed((float)(this.speedModifier * this.agent.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.agent.onGround()) {
                    this.operation = MoveControl.Operation.WAIT;
                }
            } else {
                this.agent.setZza(0.0F);
            }
        }
    }

    private void look (float xChange, float yChange) {
        //Works:
        Vec3 rawLookSpot = this.calculateViewVector(this.getXRot()+xChange, this.getYHeadRot()+yChange);
        Vec3 lookSpot = this.getEyePosition().add(rawLookSpot);
        this.getLookControl().setLookAt(lookSpot);
        this.setYRot(this.getVisualRotationYInDegrees());
    }

    @Override
    public int getMaxHeadXRot() {
        return 85;
    }

    @Override
    public int getMaxHeadYRot() {
        return 45;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
