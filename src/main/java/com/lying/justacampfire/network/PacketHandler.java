package com.lying.justacampfire.network;

import com.lying.justacampfire.reference.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(Reference.ModInfo.MOD_ID);
	private static byte packetID = 0;
	
	public static void init()
	{
		registerMessage(PacketSignalSmoke.class);
	}
	
	private static final <T extends PacketAbstract<T> & IMessageHandler<T, IMessage>> void registerMessage(Class<T> par1Packet)
	{
		if(PacketAbstract.PacketAbstractClient.class.isAssignableFrom(par1Packet))
		{
			HANDLER.registerMessage(par1Packet, par1Packet, packetID++, Side.CLIENT);
		}
		else if(PacketAbstract.PacketAbstractServer.class.isAssignableFrom(par1Packet))
		{
			HANDLER.registerMessage(par1Packet, par1Packet, packetID++, Side.SERVER);
		}
		else
		{
			HANDLER.registerMessage(par1Packet, par1Packet, packetID, Side.CLIENT);
			HANDLER.registerMessage(par1Packet, par1Packet, packetID++, Side.SERVER);
		}
	}
	
	public static final void sendTo(IMessage msg, EntityPlayerMP player)
	{
		HANDLER.sendTo(msg, player);
	}
	public static final void sendToAll(IMessage msg)
	{
		HANDLER.sendToAll(msg);
	}
	public static final void sendToNearby(IMessage msg, World world, Entity ent)
	{
		sendToNearby(msg, world, new BlockPos(ent));
	}
	public static final void sendToNearby(IMessage msg, World world, BlockPos point)
	{
		WorldServer worldServer;
		if ((world instanceof WorldServer))
		{
			worldServer = (WorldServer)world;
			for (EntityPlayer player : worldServer.playerEntities)
			{
				EntityPlayerMP playerMP = (EntityPlayerMP)player;
				if(playerMP.getDistanceSq(point) < 4096.0D)
				{
					HANDLER.sendTo(msg, playerMP);
				}
			}
		}
	}
	public static final void sendToDimension(IMessage msg, int dimID)
	{
		HANDLER.sendToDimension(msg, dimID);
	}
	public static final void sendToServer(IMessage msg)
	{
		HANDLER.sendToServer(msg);
	}
}
