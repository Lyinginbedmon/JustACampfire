package com.lying.justacampfire.proxy;

import com.lying.justacampfire.client.ParticleRegistry;
import com.lying.justacampfire.client.renderer.tileentity.TileEntityCampfireRenderer;
import com.lying.justacampfire.init.JACParticle;
import com.lying.justacampfire.tileentity.TileEntityCampfire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public void preInit(FMLPreInitializationEvent par1Event)
	{
		super.preInit(par1Event);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfire.class, new TileEntityCampfireRenderer());
	}
	
	public void init(FMLInitializationEvent par1Event)
	{
		super.init(par1Event);
	}
	public void postInit(FMLPostInitializationEvent par1Event){ super.postInit(par1Event); }
	
	public EntityPlayer getPlayerEntity(MessageContext ctx){ return (ctx.side.isClient() ? mc.player : super.getPlayerEntity(ctx)); }
	public IThreadListener getThreadFromContext(MessageContext ctx){ return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx)); }
	
	public void spawnCustomParticle(JACParticle name, World world, double posX, double posY, double posZ, double velX, double velY, double velZ, int... params)
	{
		switch(Minecraft.getMinecraft().gameSettings.particleSetting)
		{
			case 0:
				break;
			case 1:
				if(world.rand.nextBoolean())
					return;
				break;
			case 2:
				return;
		}
		
		Particle particle = ParticleRegistry.getParticle(name, world, posX, posY, posZ, velX, velY, velZ, params);
		if(particle != null) Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
}
