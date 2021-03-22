package com.lying.justacampfire.init;

import net.minecraft.util.EnumParticleTypes;

public enum JACParticle 
{
	SMOKE_SIGNAL_COLORED(1, "smoke_signal_colored");
	
	private final int particleID;
	private final String particleName;
	
	private JACParticle(int ID, String nameIn)
	{
		particleID = EnumParticleTypes.values().length + ID;
		particleName = nameIn;
	}
	
	public int getParticleID(){ return particleID; }
	
	public String getParticleName(){ return particleName; }
	
	public static JACParticle getParticleFromId(int id)
	{
		for(JACParticle particle : values())
			if(particle.getParticleID() == id)
				return particle;
		return null;
	}
}
