package com.lying.justacampfire.network;

import java.io.IOException;

import com.lying.justacampfire.JustACampfire;
import com.lying.justacampfire.init.JACParticle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSignalSmoke extends PacketAbstract<PacketSignalSmoke>
{
	double posX;
	double posY;
	double posZ;
	int dimension;
	EnumDyeColor colour;
	boolean isSignal;
	
	public PacketSignalSmoke(){ }
	public PacketSignalSmoke(double xIn, double yIn, double zIn, int dimIn, EnumDyeColor colourIn, boolean signalIn)
	{
		posX = xIn;
		posY = yIn;
		posZ = zIn;
		dimension = dimIn;
		colour = colourIn;
		isSignal = signalIn;
	}
	
	protected void write(PacketBuffer par1Buffer) throws IOException
	{
		par1Buffer.writeDouble(posX);
		par1Buffer.writeDouble(posY);
		par1Buffer.writeDouble(posZ);
		par1Buffer.writeInt(colour.getMetadata());
		par1Buffer.writeBoolean(isSignal);
	}
	
	protected void read(PacketBuffer par1Buffer) throws IOException
	{
		posX = par1Buffer.readDouble();
		posY = par1Buffer.readDouble();
		posZ = par1Buffer.readDouble();
		colour = EnumDyeColor.byMetadata(par1Buffer.readInt());
		isSignal = par1Buffer.readBoolean();
	}
	
	public void process(EntityPlayer par1Player, Side par2Side)
	{
		if(par2Side == Side.SERVER) PacketHandler.sendToAll(this);
		else
		{
	        int colourValue =	colour.getColorValue();
	        int colorRed =		(colourValue >> 16 & 255);
	        int colorGreen =	(colourValue >> 8 & 255);
	        int colorBlue =		(colourValue & 255);
	        
	        JustACampfire.proxy.spawnCustomParticle(JACParticle.SMOKE_SIGNAL_COLORED, par1Player.getEntityWorld(), posX, posY, posZ, 0D, 0D, 0D, isSignal ? 1 : 0, colorRed, colorGreen, colorBlue);
		}
	}
}
