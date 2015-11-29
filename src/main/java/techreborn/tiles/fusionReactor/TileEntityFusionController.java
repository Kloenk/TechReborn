package techreborn.tiles.fusionReactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;


public class TileEntityFusionController extends TilePowerAcceptor implements IInventory {

    public Inventory inventory = new Inventory(3, "TileEntityFusionController", 64, this);

    //0= no coils, 1 = coils
    public int coilStatus = 0;

    int topStackSlot = 0;
    int bottomStackSlot = 1;
    int outputStackSlot = 2;

    public int crafingTickTime = 0;
    public int finalTickTime = 0;
    public int neededPower = 0;

    FusionReactorRecipe currentRecipe = null;
    boolean hasStartedCrafting = false;

    public TileEntityFusionController() {
        super(4);
    }

    @Override
    public double getMaxPower() {
        return 100000000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        if(direction == EnumFacing.DOWN || direction == EnumFacing.UP){
            return false;
        }
        return true;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        if(direction == EnumFacing.DOWN || direction == EnumFacing.UP){
            return true;
        }
        return false;
    }

    @Override
    public double getMaxOutput() {
        if(!hasStartedCrafting){
            return 0;
        }
        return 1000000;
    }

    @Override
    public double getMaxInput() {
        if(hasStartedCrafting){
            return 0;
        }
        return 8192;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        crafingTickTime = tagCompound.getInteger("crafingTickTime");
        finalTickTime = tagCompound.getInteger("finalTickTime");
        neededPower = tagCompound.getInteger("neededPower");
        hasStartedCrafting = tagCompound.getBoolean("hasStartedCrafting");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);

        if(crafingTickTime == -1){
            crafingTickTime = 0;
        }
        if(finalTickTime == -1){
            finalTickTime = 0;
        }
        if(neededPower == -1){
            neededPower = 0;
        }
        tagCompound.setInteger("crafingTickTime", crafingTickTime);
        tagCompound.setInteger("finalTickTime", finalTickTime);
        tagCompound.setInteger("neededPower", neededPower);
        tagCompound.setBoolean("hasStartedCrafting", hasStartedCrafting);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return inventory.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }


    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }


    public boolean checkCoils() {
        if ((isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() + 1)) &&
                (isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ())) &&
                (isCoil(this.getPos().getX() + 3, this.getPos().getY(), this.getPos().getZ() - 1)) &&
                (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() + 1)) &&
                (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ())) &&
                (isCoil(this.getPos().getX() - 3, this.getPos().getY(), this.getPos().getZ() - 1)) &&
                (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 2)) &&
                (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() + 1)) &&
                (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 1)) &&
                (isCoil(this.getPos().getX() + 2, this.getPos().getY(), this.getPos().getZ() - 2)) &&
                (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 2)) &&
                (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() + 1)) &&
                (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 1)) &&
                (isCoil(this.getPos().getX() - 2, this.getPos().getY(), this.getPos().getZ() - 2)) &&
                (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 3)) &&
                (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 2)) &&
                (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 2)) &&
                (isCoil(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() - 3)) &&
                (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 3)) &&
                (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() + 2)) &&
                (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 2)) &&
                (isCoil(this.getPos().getX() - 1, this.getPos().getY(), this.getPos().getZ() - 3)) &&
                (isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() + 3)) &&
                (isCoil(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() - 3))) {
            coilStatus = 1;
            return true;
        }
        coilStatus = 0;
        return false;
    }

    private boolean isCoil(int x, int y, int z) {
        return worldObj.getBlockState(new BlockPos(x, y, z)).getBlock() == ModBlocks.FusionCoil;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        //TODO improve this code a lot

        if (worldObj.getTotalWorldTime() % 20 == 0) {
            checkCoils();
        }

        if (!worldObj.isRemote) {
            if (coilStatus == 1) {
                if (currentRecipe == null) {
                    if (inventory.hasChanged || crafingTickTime != 0) {
                        for (FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
                            if (ItemUtils.isItemEqual(getStackInSlot(topStackSlot), reactorRecipe.getTopInput(), true, true, true)) {
                                if (reactorRecipe.getBottomInput() != null) {
                                    if (ItemUtils.isItemEqual(getStackInSlot(bottomStackSlot), reactorRecipe.getBottomInput(), true, true, true) == false) {
                                        break;
                                    }
                                }
                                if (canFitStack(reactorRecipe.getOutput(), outputStackSlot, true)) {
                                    currentRecipe = reactorRecipe;
                                    if(crafingTickTime != 0){
                                        finalTickTime = currentRecipe.getTickTime();
                                        neededPower = (int) currentRecipe.getStartEU();
                                    }
                                    hasStartedCrafting = false;
                                    crafingTickTime = 0;
                                    finalTickTime = currentRecipe.getTickTime();
                                    neededPower = (int) currentRecipe.getStartEU();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if (inventory.hasChanged) {
                        if (!validateRecipe()) {
                            resetCrafter();
                            return;
                        }
                    }
                    if (!hasStartedCrafting) {
                        if (canUseEnergy(currentRecipe.getStartEU() + 64)) {
                            useEnergy(currentRecipe.getStartEU());
                            hasStartedCrafting = true;
                        }
                    } else {
                        if (crafingTickTime < currentRecipe.getTickTime()) {
                            if (currentRecipe.getEuTick() > 0) { //Power gen
                                addEnergy(currentRecipe.getEuTick()); //Waste power if it has no where to go
                                crafingTickTime++;
                            } else { //Power user
                                if (canUseEnergy(currentRecipe.getEuTick() * -1)) {
                                    setEnergy(getEnergy() - (currentRecipe.getEuTick() * -1));
                                    crafingTickTime++;
                                }
                            }
                        } else {
                            if (canFitStack(currentRecipe.getOutput(), outputStackSlot, true)) {
                                if (getStackInSlot(outputStackSlot) == null) {
                                    setInventorySlotContents(outputStackSlot, currentRecipe.getOutput().copy());
                                } else {
                                    decrStackSize(outputStackSlot, -currentRecipe.getOutput().stackSize);
                                }
                                decrStackSize(topStackSlot, currentRecipe.getTopInput().stackSize);
                                if (currentRecipe.getBottomInput() != null) {
                                    decrStackSize(bottomStackSlot, currentRecipe.getBottomInput().stackSize);
                                }
                                resetCrafter();
                            }
                        }
                    }
                }
            } else {
                if (currentRecipe != null) {
                    resetCrafter();
                }
            }
        }


        if (inventory.hasChanged) {
            inventory.hasChanged = false;
        }
    }

    private boolean validateRecipe() {
        if (ItemUtils.isItemEqual(getStackInSlot(topStackSlot), currentRecipe.getTopInput(), true, true, true)) {
            if (currentRecipe.getBottomInput() != null) {
                if (ItemUtils.isItemEqual(getStackInSlot(bottomStackSlot), currentRecipe.getBottomInput(), true, true, true) == false) {
                    return false;
                }
            }
            if (canFitStack(currentRecipe.getOutput(), outputStackSlot, true)) {
                return true;
            }
        }
        return false;
    }

    private void resetCrafter() {
        currentRecipe = null;
        crafingTickTime = -1;
        finalTickTime = -1;
        neededPower = -1;
        hasStartedCrafting = false;
    }

    public boolean canFitStack(ItemStack stack, int slot, boolean oreDic) {//Checks to see if it can fit the stack
        if (stack == null) {
            return true;
        }
        if (inventory.getStackInSlot(slot) == null) {
            return true;
        }
        if (ItemUtils.isItemEqual(inventory.getStackInSlot(slot), stack, true, true, oreDic)) {
            if (stack.stackSize + inventory.getStackInSlot(slot).stackSize <= stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
