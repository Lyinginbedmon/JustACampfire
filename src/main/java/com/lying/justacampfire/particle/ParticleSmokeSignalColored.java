package com.lying.justacampfire.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleSmokeSignalColored extends ParticleSmokeSignal
{
    
    private ParticleSmokeSignalColored(TextureManager textureManagerIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double velX, double velY, double velZ, boolean signal, float red, float green, float blue)
    {
    	super(textureManagerIn, worldIn, xCoordIn, yCoordIn, zCoordIn, velX, velY, velZ, signal);

    	this.particleRed = red > 1F ? red / 255F : red;
    	this.particleGreen = green > 1F ? green / 255F : red;
    	this.particleBlue = blue > 1F ? blue / 255F : red;
    }
    
    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... params)
        {
        	boolean isSignal = false;
        	float red = 1F, green = 1F, blue = 1F;
        	if(params != null)
        	{
        		isSignal = params.length > 0 && params[0] > 0;
        		red = (params.length > 1) ? params[1] : 0;
        		green = (params.length > 2) ? params[2] : 0;
        		blue = (params.length > 3) ? params[3] : 0;
        	}
            return new ParticleSmokeSignalColored(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, isSignal, red, green, blue);
        }
    }
}
