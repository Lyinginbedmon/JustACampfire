package com.lying.justacampfire.block;

import com.lying.justacampfire.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockVO extends Block
{
	public BlockVO(String nameIn, Material materialIn)
	{
		super(materialIn);
		setBlockDefaults(nameIn);
	}
	
	public BlockVO(String nameIn, Material materialIn, MapColor colorIn)
	{
		super(materialIn, colorIn);
		setBlockDefaults(nameIn);
	}
	
	public void setBlockDefaults(String nameIn)
	{
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setRegistryName(Reference.ModInfo.MOD_ID, nameIn);
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	public Block setSoundType(SoundType typeIn)
	{
		super.setSoundType(typeIn);
		return this;
	}
}
