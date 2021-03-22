package com.lying.justacampfire.particle;

import com.lying.justacampfire.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSmokeSignal extends ParticleJAC
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ModInfo.MOD_ID, "textures/particle/smoke_signal.png");
    
    protected ParticleSmokeSignal(TextureManager textureManagerIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double velX, double velY, double velZ, boolean signal)
    {
        super(textureManagerIn, worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        
        this.motionX += velX;
        this.motionY += velY;
        this.motionZ += velZ;
        
        float f = (float)(Math.random() * 0.75F) + 0.25F;
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        
        this.particleScale *= 2.5F;
        
        this.particleMaxAge = Reference.Values.TICKS_PER_SECOND * (signal ? 15 : 5);
        this.setParticleTextureIndex(11);
    }
    
    /** The texture file to use for this particle */
    public ResourceLocation getTexture(){ return TEXTURE; }
    
    public int getIconSize(){ return 16; }
    
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if(this.particleAge++ >= this.particleMaxAge) this.setExpired();
        
        int opacityStart = this.particleMaxAge / 4 * 3;
        if(this.particleAge > opacityStart)
        {
        	int range = this.particleMaxAge - opacityStart;
        	this.particleAlpha = 1F - (float)(this.particleAge - opacityStart) / (float)range;
        }
        
        this.setParticleTextureIndex(11 - this.particleAge * 12 / this.particleMaxAge);
        
        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);
        
        if(this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }
        
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
        
        if(this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... params)
        {
        	boolean isSignal = (params != null && params.length > 0) && params[0] > 0;
            return new ParticleSmokeSignal(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, isSignal);
        }
    }
}
