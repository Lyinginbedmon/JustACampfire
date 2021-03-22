package com.lying.justacampfire.particle;

import com.lying.justacampfire.reference.Reference;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ParticleJAC extends Particle
{
	protected static final ResourceLocation VANILLA = new ResourceLocation("minecraft:textures/particle/particles.png");
	protected static final ResourceLocation PARTICLES = new ResourceLocation(Reference.ModInfo.MOD_ID, "textures/particle/particles.png");
	
    /** The Rendering Engine. */
    private final TextureManager textureManager;
	
	public ParticleJAC(TextureManager textureManagerIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
	{
		this(textureManagerIn, worldIn, xCoordIn, yCoordIn, zCoordIn, 0D, 0D, 0D);
	}
	
	public ParticleJAC(TextureManager textureManagerIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.textureManager = textureManagerIn;
	}
	
    public int getFXLayer()
    {
        return 3;
    }
    
    /**
     * Public method to set private field particleTextureIndex.
     */
    public void setParticleTextureIndex(int particleTextureIndex)
    {
    	particleTextureIndex = Math.max(0, particleTextureIndex);
    	int iconScale = 128 / getIconSize();
        this.particleTextureIndexX = particleTextureIndex % iconScale;
        this.particleTextureIndexY = particleTextureIndex / iconScale;
    }
    
    /** The texture file to use for this particle */
    public ResourceLocation getTexture(){ return PARTICLES; }
    
    /** The number of icons by row and column */
    public int getIconSize(){ return 8; }
    
    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    	this.textureManager.bindTexture(getTexture());
    	
    	float iconScale = 128F / (float)getIconSize();
        double minX = (float)this.particleTextureIndexX / iconScale;
        double maxX = minX + (1F / iconScale);
        double minY = (float)this.particleTextureIndexY / iconScale;
        double maxY = minY + (1F / iconScale);
        
        double posX = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        double posY = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        double posZ = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        
        float scale = 0.1F * this.particleScale;
        Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-rotationX * scale - rotationXY * scale), (double)(-rotationZ * scale), (double)(-rotationYZ * scale - rotationXZ * scale)), new Vec3d((double)(-rotationX * scale + rotationXY * scale), (double)(rotationZ * scale), (double)(-rotationYZ * scale + rotationXZ * scale)), new Vec3d((double)(rotationX * scale + rotationXY * scale), (double)(rotationZ * scale), (double)(rotationYZ * scale + rotationXZ * scale)), new Vec3d((double)(rotationX * scale - rotationXY * scale), (double)(-rotationZ * scale), (double)(rotationYZ * scale - rotationXZ * scale))};
        if(this.particleAngle != 0.0F)
        {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.z;
            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);
            for (int l = 0; l < 4; ++l)
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
        }
        
        int brightness = this.getBrightnessForRender(partialTicks);
        int j = brightness >> 16 & 65535;
        int k = brightness & 65535;
        
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	        buffer.pos(posX + avec3d[0].x, posY + avec3d[0].y, posZ + avec3d[0].z).tex(maxX, maxY).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	        buffer.pos(posX + avec3d[1].x, posY + avec3d[1].y, posZ + avec3d[1].z).tex(maxX, minY).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	        buffer.pos(posX + avec3d[2].x, posY + avec3d[2].y, posZ + avec3d[2].z).tex(minX, minY).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
	        buffer.pos(posX + avec3d[3].x, posY + avec3d[3].y, posZ + avec3d[3].z).tex(minX, maxY).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
    }
}
