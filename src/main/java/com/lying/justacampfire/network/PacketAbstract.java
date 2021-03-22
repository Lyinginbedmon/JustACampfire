package com.lying.justacampfire.network;

import java.io.IOException;

import com.google.common.base.Throwables;
import com.lying.justacampfire.JustACampfire;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("deprecation")
public abstract class PacketAbstract<T extends PacketAbstract<T>> implements IMessage, IMessageHandler<T, IMessage>
{
	protected abstract void read(PacketBuffer par1Buffer) throws IOException;
	
	protected abstract void write(PacketBuffer par1Buffer) throws IOException;
	
	public abstract void process(EntityPlayer par1Player, Side par2Side);
	
	protected boolean isValidOnSide(Side par1Side){ return true; }
	
	protected boolean requiresMainThread(){ return true; }
	
	public void fromBytes(ByteBuf par1Buffer)
	{
		try{ read(new PacketBuffer(par1Buffer)); }
		catch (IOException e){ throw Throwables.propagate(e); }
	}
	
	public void toBytes(ByteBuf par1Buffer)
	{
		try{ write(new PacketBuffer(par1Buffer)); }
		catch (IOException e){ throw Throwables.propagate(e); }
	}
	
	public final IMessage onMessage(T msg, MessageContext ctx)
	{
		if(!msg.isValidOnSide(ctx.side))
		{
			throw new RuntimeException("Invalid side "+ctx.side.name()+" for "+msg.getClass().getSimpleName());
		}
		else if(msg.requiresMainThread()){ checkThreadAndEnqeue(msg, ctx); }
		else{ msg.process(JustACampfire.proxy.getPlayerEntity(ctx), ctx.side); }
		return null;
	}
	
	private static final <T extends PacketAbstract<T>> void checkThreadAndEnqeue(final PacketAbstract<T> msg, final MessageContext ctx)
	{
		IThreadListener thread = JustACampfire.proxy.getThreadFromContext(ctx);
		thread.addScheduledTask(new Runnable()
			{
				public void run()
				{
					msg.process(JustACampfire.proxy.getPlayerEntity(ctx), ctx.side);
				}
			});
	}
	
	public static abstract class PacketAbstractClient<T extends PacketAbstract<T>> extends PacketAbstract<T>
	{
		@Override
		protected final boolean isValidOnSide(Side side){ return side.isClient(); }
	}
	
	public static abstract class PacketAbstractServer<T extends PacketAbstract<T>> extends PacketAbstract<T>
	{
		@Override
		protected final boolean isValidOnSide(Side side){ return side.isServer(); }
	}
}
