package com.lying.justacampfire.utility.bus;

import com.lying.justacampfire.block.BlockVORotated;
import com.lying.justacampfire.init.JACBlocks;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles various special events server-side
 * @author Lying
 */
public class BusServer
{
	@SubscribeEvent
	public static void onRightClickBlockEvent(RightClickBlock event)
	{
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		EntityLivingBase user = event.getEntityLiving();
		if(user.isSneaking())
		{
			ItemStack heldItem = user.getHeldItem(event.getHand());
			if(heldItem.isEmpty()) return;
			
			Item theItem = heldItem.getItem();
			if(event.getHitVec() == null) return;
			
			Vec3d hitVec = event.getHitVec();
			BlockPos targetPos = new BlockPos(hitVec.x, hitVec.y, hitVec.z);
			if(theItem == Items.STICK)
			{
				BlockPos blockBelow = targetPos.down();
				if(!targetPos.equals(pos.up()) || world.getBlockState(blockBelow).getBlockFaceShape(world, blockBelow, EnumFacing.UP) != BlockFaceShape.SOLID) return;
				if(world.isAirBlock(targetPos) || world.getBlockState(targetPos).getBlock().isReplaceable(world, targetPos))
				{
					world.setBlockState(targetPos, JACBlocks.CAMPFIRE_UNLIT.getDefaultState().withProperty(BlockVORotated.FACING, user.getHorizontalFacing()));
					
					if(user instanceof EntityPlayer && ((EntityPlayer)user).isCreative()) ;
					else heldItem.shrink(1);
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);
				}
			}
		}
	}
}
