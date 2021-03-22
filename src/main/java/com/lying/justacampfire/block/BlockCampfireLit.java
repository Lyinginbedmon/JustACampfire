package com.lying.justacampfire.block;

import java.util.Random;

import com.lying.justacampfire.init.JACBlocks;
import com.lying.justacampfire.tileentity.TileEntityCampfire;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCampfireLit extends BlockCampfireBase implements ITileEntityProvider
{
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class, EnumDyeColor.WHITE, EnumDyeColor.RED, EnumDyeColor.GREEN, EnumDyeColor.YELLOW);
	
	public BlockCampfireLit()
	{
		super("campfire_lit");
		setDefaultState(getDefaultState().withProperty(COLOR, EnumDyeColor.WHITE));
		setLightLevel(1.0F);
		setTickRandomly(true);
	}
    
    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.TNT;
    }
    
    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)entityIn))
        {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
            if(worldIn.rand.nextInt(5) == 0) entityIn.setFire(3);
        }
        
        super.onEntityWalk(worldIn, pos, entityIn);
    }
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	TileEntityCampfire tile = (TileEntityCampfire)worldIn.getTileEntity(pos);
    	if(!worldIn.isRemote && tile != null && !tile.isEmpty())
    	{
    		Random rand = worldIn.rand;
    		for(ItemStack stack : tile.getContents())
    			if(!stack.isEmpty())
				{
    				EntityItem item = new EntityItem(worldIn, pos.getX() + rand.nextDouble(), pos.getY() + 0.5D, pos.getZ() + rand.nextDouble(), stack);
    				item.motionX = (rand.nextDouble() - 0.5D);
    				item.motionZ = (rand.nextDouble() - 0.5D);
    				worldIn.spawnEntity(item);
				}
    	}
    	
    	super.breakBlock(worldIn, pos, state);
    }
    
    public static void extinguish(World worldIn, BlockPos pos)
    {
    	IBlockState originalState = worldIn.getBlockState(pos);
    	if(originalState.getBlock() != JACBlocks.CAMPFIRE_LIT) return;
    	
    	EnumFacing face = originalState.getValue(BlockVORotated.FACING);
        worldIn.setBlockState(pos, JACBlocks.CAMPFIRE_BURNT.getDefaultState().withProperty(BlockVORotated.FACING, face), 11);
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
        if(worldIn instanceof WorldServer) ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.25D, (double)pos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
    }
    
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if(!heldItem.isEmpty() && heldItem.getItem() == Items.WATER_BUCKET)
        {
        	extinguish(worldIn, pos);
            if(!playerIn.isCreative())
            	playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
            return true;
        }
        
    	TileEntityCampfire tile = (TileEntityCampfire)worldIn.getTileEntity(pos);
    	if(tile != null && facing == EnumFacing.UP)
    		tile.onInteract(worldIn, playerIn, heldItem, hitX, hitZ, facing);
        
        return true;
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        BlockPos blockpos = pos.up();
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if(iblockstate.getBlock() == Blocks.WATER || iblockstate.getBlock() == Blocks.FLOWING_WATER)
        {
            worldIn.setBlockToAir(blockpos);
        	extinguish(worldIn, pos);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	double posX = pos.getX();
    	double posY = pos.getY();
    	double posZ = pos.getZ();
        if(rand.nextInt(24) == 0)
            worldIn.playSound((double)((float)posX + 0.5F), (double)((float)posY + 0.5F), (double)((float)posZ + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCampfire();
	}
	
	public static int getColorID(EnumDyeColor colorIn)
	{
        switch(colorIn)
        {
			case RED:		return 1;
			case GREEN:		return 2;
			case YELLOW:	return 3;
			default:	return 0;
        }
	}
	
	public static EnumDyeColor getColor(IBlockState state)
	{
		return state.getValue(COLOR);
	}
	public static EnumDyeColor getColor(int colorIn)
	{
		switch(colorIn)
		{
			case 0:		return EnumDyeColor.RED;
			case 1:		return EnumDyeColor.GREEN;
			case 2:		return EnumDyeColor.YELLOW;
			default:	return EnumDyeColor.WHITE;
		}
	}
	
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta = meta | ((EnumFacing)state.getValue(BlockVORotated.FACING)).getHorizontalIndex();
    	meta |= getColorID(getColor(state)) << 2;
        return meta;
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockVORotated.FACING, EnumFacing.getHorizontal(meta)).withProperty(COLOR, getColor(meta >> 2));
    }
	
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BlockVORotated.FACING, COLOR});
    }
}
