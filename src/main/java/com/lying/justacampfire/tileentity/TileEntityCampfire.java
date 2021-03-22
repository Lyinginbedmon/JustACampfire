package com.lying.justacampfire.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import com.lying.justacampfire.block.BlockCampfireLit;
import com.lying.justacampfire.init.JACBlocks;
import com.lying.justacampfire.network.PacketHandler;
import com.lying.justacampfire.network.PacketSignalSmoke;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class TileEntityCampfire extends TileEntity implements ITickable
{
	private int ticksToExtinguish = -1;
	
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    private int[] itemBurnTimes = new int[]{0, 0, 0, 0};
    
    public TileEntityCampfire()
    {
    	
    }
    
    public boolean isCookable(ItemStack item)
    {
    	return item.getItem() instanceof ItemFood && !FurnaceRecipes.instance().getSmeltingResult(item).isEmpty();
    }
    
	public void update()
	{
		if(getWorld().getGameRules().getBoolean("doFireTick"))
			if(ticksToExtinguish > 0 && --ticksToExtinguish == 0)
			{
				BlockCampfireLit.extinguish(getWorld(), getPos());
				return;
			}
		
		if(getWorld().getBlockState(getPos()).getBlock() == JACBlocks.CAMPFIRE_LIT)
		{
			int slot = 0;
			for(ItemStack item : furnaceItemStacks)
			{
				if(item.isEmpty() || !isCookable(item)) itemBurnTimes[slot] = 0;
				else if(isCookable(item))
				{
					if(itemBurnTimes[slot] >= 200)
						setContentsOfSlot(slot, FurnaceRecipes.instance().getSmeltingResult(item).copy());
					else
					{
						itemBurnTimes[slot] = itemBurnTimes[slot] + 1;
						
						if(getWorld().isRemote && getWorld().rand.nextInt(4) == 0)
						{
							Tuple<Double, Double> slotOffset = getSlotOffset(slot);
							world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, getPos().getX() + slotOffset.getFirst(), getPos().getY() + 0.4375D, getPos().getZ() + slotOffset.getSecond(), 0D, 0D, 0D);
						}
					}
				}
				slot++;
			}
			
			if(!getWorld().isRemote)
			{
				Random rand = getWorld().rand;
				if(rand.nextInt(10) == 0)
				{
			        double d0 = (double)getPos().getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.4D;
			        double d1 = (double)getPos().getY() + 0.5D + (rand.nextDouble() * 0.5D);
			        double d2 = (double)getPos().getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.4D;
			        
			        PacketHandler.sendToAll(new PacketSignalSmoke(d0, d1, d2, getWorld().provider.getDimension(), getColor(), getWorld().getBlockState(getPos().down()).getBlock() == Blocks.HAY_BLOCK));
				}
			}
		}
		else getWorld().removeTileEntity(getPos());
	}
	
	public EnumDyeColor getColor()
	{
		IBlockState state = getWorld().getBlockState(getPos());
		return state.getBlock() == JACBlocks.CAMPFIRE_LIT ? BlockCampfireLit.getColor(state) : EnumDyeColor.WHITE;
	}
	
	public void onInteract(World world, EntityPlayer player, ItemStack heldItem, float hitX, float hitZ, EnumFacing face)
	{
		if(heldItem.getItem() instanceof ItemDye)
		{
			EnumDyeColor color = EnumDyeColor.byDyeDamage(heldItem.getMetadata());
			if(color == EnumDyeColor.RED || color == EnumDyeColor.GREEN || color == EnumDyeColor.YELLOW || color == EnumDyeColor.WHITE)
			{
				IBlockState state = getWorld().getBlockState(getPos());
				if(state.getBlock() == JACBlocks.CAMPFIRE_LIT)
				{
					getWorld().setBlockState(getPos(), state.withProperty(BlockCampfireLit.COLOR, color));
					this.validate();
					getWorld().setTileEntity(getPos(), this);
				}
				
				if(!player.isCreative())
					heldItem.shrink(1);
				
				return;
			}
		}
		
		int slot = getSlotByHit(hitX, hitZ);
		if(slot != -1 && face == EnumFacing.UP)
		{
			ItemStack stackInSlot = getContentsOfSlot(slot);
			if(stackInSlot.isEmpty() && isCookable(heldItem))
			{
				ItemStack stack = heldItem.copy();
				stack.setCount(1);
				setContentsOfSlot(slot, stack);
				if(!player.isCreative())
					heldItem.shrink(1);
				return;
			}
			else if(!stackInSlot.isEmpty())
			{
				EntityItem item = new EntityItem(world, getPos().getX() + hitX, getPos().getY() + 0.5D, getPos().getZ() + hitZ, stackInSlot);
				if(!world.isRemote)
					world.spawnEntity(item);
				setContentsOfSlot(slot, ItemStack.EMPTY);
				itemBurnTimes[slot] = 0;
				
				return;
			}
		}
	}
	
	public int getSlotByHit(float hitX, float hitZ)
	{
		if(hitX > 0.3F && hitX < 0.6F || hitZ > 0.3F && hitZ < 0.6F) return -1;
		return (hitX > 0.5F ? 1 : 0) + (hitZ > 0.5F ? 2 : 0);
	}
	
	public Tuple<Double, Double> getSlotOffset(int slot)
	{
		return new Tuple<Double, Double>(slot%2 == 1 ? 0.85D : 0.15D, slot > 1 ? 0.85D : 0.15D);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", this.ticksToExtinguish);
		
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
        
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < itemBurnTimes.length; ++i) nbttaglist.appendTag(new NBTTagInt(itemBurnTimes[i]));
        compound.setTag("CookTimes", nbttaglist);
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		ticksToExtinguish = compound.getInteger("BurnTime");
		
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        
        NBTTagList tagList = compound.getTagList("CookTimes", 3);
        for(int i=0; i<tagList.tagCount(); i++) this.itemBurnTimes[i] = tagList.getIntAt(i);
	}
	
	public void setBurnTimeRemaining(int par1Int){ this.ticksToExtinguish = par1Int; }
	
	public boolean isEmpty()
	{
		for(ItemStack stack : furnaceItemStacks) if(!stack.isEmpty()) return false;
		return true;
	}
	
	public ItemStack getContentsOfSlot(int slot){ return this.furnaceItemStacks.get(slot); }
	public void setContentsOfSlot(int slot, ItemStack stack)
	{
		this.furnaceItemStacks.set(slot, stack);
		this.markDirty();
	}
	
	public NonNullList<ItemStack> getContents(){ return this.furnaceItemStacks; }
	
	public void markDirty()
	{
		super.markDirty();
		if(getWorld() != null && !getWorld().isRemote)
			for(EntityPlayer player : getWorld().playerEntities)
				if(player.getDistanceSq(getPos()) < (64 * 64)) ((EntityPlayerMP)player).connection.sendPacket(getUpdatePacket());
	}
	
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
    	this.readFromNBT(pkt.getNbtCompound());
    }

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}
    
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
}
