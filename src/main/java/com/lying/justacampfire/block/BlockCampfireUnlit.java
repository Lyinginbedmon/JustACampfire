package com.lying.justacampfire.block;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.lying.justacampfire.init.JACBlocks;
import com.lying.justacampfire.reference.Reference;
import com.lying.justacampfire.tileentity.TileEntityCampfire;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCampfireUnlit extends BlockCampfireBase
{
    public static final PropertyInteger STICKS = PropertyInteger.create("sticks", 0, 3);
    
    protected static final Map<Tuple<EnumFacing, Integer>, AxisAlignedBB> BOXES = new HashMap<>();
	
	public BlockCampfireUnlit()
	{
		super("campfire_unlit");
		setCreativeTab(null);
		setDefaultState(getDefaultState().withProperty(STICKS, 0));
	}
	
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
    	Tuple<EnumFacing, Integer> mapping = new Tuple<EnumFacing, Integer>(blockState.getValue(BlockVORotated.FACING), blockState.getValue(STICKS).intValue());
    	for(Tuple<EnumFacing, Integer> map : BOXES.keySet())
    		if(map.getFirst() == mapping.getFirst() && map.getSecond() == mapping.getSecond()) return BOXES.get(map);
    	
    	return BOUNDING_BOX_2;
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	switch(state.getValue(STICKS).intValue())
    	{
			case 0:
			case 1:		return BOUNDING_BOX_1;
			default:	return BOUNDING_BOX_2;
    	}
    }
	
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
    	drops.add(new ItemStack(Items.STICK, state.getValue(STICKS).intValue() + 1));
    }
	
    /**
     * Called when the block is right clicked by a player.
     */
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if(!heldItem.isEmpty())
        {
        	Item item = heldItem.getItem();
        	int sticks = state.getValue(STICKS).intValue();
        	if(sticks < 3)
        	{
		        if(item == Items.STICK)
		        {
		        	worldIn.setBlockState(pos, state.withProperty(STICKS, sticks + 1), 2);
		        	TileEntityCampfire tile = new TileEntityCampfire();
		        	tile.setWorld(worldIn);
		        	tile.setPos(pos);
		        	tile.setBurnTimeRemaining(Reference.Values.TICKS_PER_MINUTE * 10);
		        	worldIn.setTileEntity(pos, tile);
		            if(!playerIn.capabilities.isCreativeMode) heldItem.shrink(1);
		            return true;
		        }
        	}
	        else if(item == Items.FLINT || item == Items.FLINT_AND_STEEL)
	        {
	        	worldIn.setBlockState(pos, JACBlocks.CAMPFIRE_LIT.getDefaultState().withProperty(BlockVORotated.FACING, state.getValue(BlockVORotated.FACING)), 2);
	            if(!playerIn.capabilities.isCreativeMode)
	            	if(item == Items.FLINT) heldItem.shrink(1);
	            	else heldItem.damageItem(1, playerIn);
	            return true;
	        }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta = meta | ((EnumFacing)state.getValue(BlockVORotated.FACING)).getHorizontalIndex();
    	meta |= state.getValue(STICKS) << 2;
        return meta;
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockVORotated.FACING, EnumFacing.getHorizontal(meta)).withProperty(STICKS, Integer.valueOf((meta >> 2)));
    }
    
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BlockVORotated.FACING, STICKS});
    }
    
    private static void registerBox(EnumFacing face, int sticks, AxisAlignedBB box)
    {
    	BOXES.put(new Tuple<EnumFacing, Integer>(face, sticks), box);
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
    	return BlockFaceShape.UNDEFINED;
    }
    
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	BlockPos posDown = pos.down();
    	IBlockState stateDown = worldIn.getBlockState(posDown);
    	if(!stateDown.isSideSolid(worldIn, posDown, EnumFacing.UP))
    	{
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
    	}
    }
    
    static
    {
    	registerBox(EnumFacing.NORTH, 0, new AxisAlignedBB( 1/16D, 0D, 0D,	5/16D, 0.25D, 1D	));
    	registerBox(EnumFacing.NORTH, 1, new AxisAlignedBB( 1/16D, 0D, 0D,	15/16D, 0.25D, 1D	));
    	registerBox(EnumFacing.NORTH, 2, new AxisAlignedBB( 0D, 0D, 0D,		1D, 7/16D, 1D		));
    	registerBox(EnumFacing.NORTH, 3, new AxisAlignedBB( 0D, 0D, 0D,		1D, 7/16D, 1D		));
    	
    	registerBox(EnumFacing.SOUTH, 0, new AxisAlignedBB( 11/16D, 0D, 0D,	15/16D, 0.25D, 1D	));
    	registerBox(EnumFacing.SOUTH, 1, new AxisAlignedBB( 1/16D, 0D, 0D,	15/16D, 0.25D, 1D	));
    	registerBox(EnumFacing.SOUTH, 2, new AxisAlignedBB( 0D, 0D, 0D,		1D, 7/16D, 1D		));
    	registerBox(EnumFacing.SOUTH, 3, new AxisAlignedBB( 0D, 0D, 0D,		1D, 7/16D, 1D		));
    	
    	registerBox(EnumFacing.EAST, 0, new AxisAlignedBB( 0D, 0D, 1/16D,	1D, 0.25D, 5/16D	));
    	registerBox(EnumFacing.EAST, 1, new AxisAlignedBB( 0D, 0D, 1/16D,	1D, 0.25D, 15/16D	));
    	registerBox(EnumFacing.EAST, 2, new AxisAlignedBB( 0D, 0D, 0D,		15/16D, 7/16D, 1D	));
    	registerBox(EnumFacing.EAST, 3, new AxisAlignedBB( 0D, 0D, 0D,		15/16D, 7/16D, 1D	));
    	
    	registerBox(EnumFacing.WEST, 0, new AxisAlignedBB( 0D, 0D, 11/16D,	1D, 0.25D, 15/16D	));
    	registerBox(EnumFacing.WEST, 1, new AxisAlignedBB( 0D, 0D, 1/16D,	1D, 0.25D, 15/16D	));
    	registerBox(EnumFacing.WEST, 2, new AxisAlignedBB( 0D, 0D, 0D,		15/16D, 7/16D, 1D	));
    	registerBox(EnumFacing.WEST, 3, new AxisAlignedBB( 0D, 0D, 0D,		15/16D, 7/16D, 1D	));
    }
}
