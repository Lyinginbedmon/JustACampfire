package com.lying.justacampfire.client;

import java.util.HashMap;
import java.util.Map;

import com.lying.justacampfire.init.JACParticle;
import com.lying.justacampfire.particle.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRegistry
{
	private static final Map<JACParticle, IParticleFactory> FACTORIES = new HashMap<>();
	
	public static IParticleFactory getParticleFactory(JACParticle name)
	{
		if(FACTORIES.containsKey(name)) return FACTORIES.get(name);
		return null;
	}
	
	public static Particle getParticle(JACParticle name, World world, double posX, double posY, double posZ, double velX, double velY, double velZ, int... params)
	{
		IParticleFactory factory = getParticleFactory(name);
		if(factory != null) return factory.createParticle(0, world, posX, posY, posZ, velX, velY, velZ, params);
		return null;
	}
	
	private static void registerParticle(JACParticle name, IParticleFactory factory)
	{
		FACTORIES.put(name, factory);
		Minecraft.getMinecraft().effectRenderer.registerParticle(name.getParticleID(), factory);
	}
	
	static
	{
		registerParticle(JACParticle.SMOKE_SIGNAL_COLORED, new ParticleSmokeSignalColored.Factory());
	}
}
