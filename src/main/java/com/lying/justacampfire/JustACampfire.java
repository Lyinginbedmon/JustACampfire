package com.lying.justacampfire;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lying.justacampfire.network.PacketHandler;
import com.lying.justacampfire.proxy.CommonProxy;
import com.lying.justacampfire.reference.Reference;
import com.lying.justacampfire.utility.bus.BusServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.ModInfo.MOD_ID, name = Reference.ModInfo.NAME, version = Reference.ModInfo.VERSION, acceptedMinecraftVersions="[1.12.2]")
public class JustACampfire
{
    @SidedProxy(clientSide = Reference.Proxies.CLIENT_PROXY_CLASS, serverSide = Reference.Proxies.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    @Instance(value = Reference.ModInfo.MOD_ID)
    public static JustACampfire instance;
    
	public static final Logger log = LogManager.getLogger(Reference.ModInfo.NAME);
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws MissingModsException
    {
    	proxy.preInit(event);
	    PacketHandler.init();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	proxy.init(event);
	    
	    MinecraftForge.EVENT_BUS.register(BusServer.class);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	
    }
}
