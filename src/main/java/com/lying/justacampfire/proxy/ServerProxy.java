package com.lying.justacampfire.proxy;

import com.lying.justacampfire.init.JACParticle;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy
{
	public void spawnCustomParticle(JACParticle name, World world, double posX, double posY, double posZ, double velX, double velY, double velZ, int... params){ }
	
	public void preInit(FMLPreInitializationEvent par1Event)
	{
		super.preInit(par1Event);
	}
	
	public void init(FMLInitializationEvent par1Event)
	{
		super.init(par1Event);
	}
	
	public void postInit(FMLPostInitializationEvent par1Event)
	{
		super.postInit(par1Event);
	}
}
