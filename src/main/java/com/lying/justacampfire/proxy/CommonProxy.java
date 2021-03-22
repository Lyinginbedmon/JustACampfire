package com.lying.justacampfire.proxy;

import com.lying.justacampfire.init.JACParticle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mod.EventBusSubscriber
public abstract class CommonProxy implements IProxy
{
	public void preInit(FMLPreInitializationEvent par1Event)
	{
		
	}
	
	public void init(FMLInitializationEvent par1Event)
	{
		
	}
	
	public void postInit(FMLPostInitializationEvent par1Event)
	{
		
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.getServerHandler().player;
	}
	
	public IThreadListener getThreadFromContext(MessageContext ctx)
	{
		return getPlayerEntity(ctx).getServer();
	}
	
	public void spawnCustomParticle(JACParticle particle, World world, double posX, double posY, double posZ, double velX, double velY, double velZ)
	{
		spawnCustomParticle(particle, world, posX, posY, posZ, velX, velY, velZ, null);
	}
	public abstract void spawnCustomParticle(JACParticle particle, World world, double posX, double posY, double posZ, double velX, double velY, double velZ, int... params);
}
