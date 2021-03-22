package com.lying.justacampfire.reference;

public class Reference
{
	/**
	 * Mod information
	 */
	public static class ModInfo
	{
		public static final String NAME					 = "Just A Campfire";
		public static final String MOD_ID				 = "jac";
		public static final String VERSION				 = "1.0";
		
		public static final String MOD_PREFIX			 = MOD_ID + ":";
	}
	
	/**
	 * Proxy class string locations
	 */
	public static class Proxies
	{
		public static final String CLIENT_PROXY_CLASS	 = "com.lying.justacampfire.proxy.ClientProxy";
		public static final String SERVER_PROXY_CLASS	 = "com.lying.justacampfire.proxy.ServerProxy";
		public static final String COMMON_PROXY_CLASS	 = "com.lying.justacampfire.proxy.CommonProxy";
	}
	
	public static class Values
	{
		public static final int TICKS_PER_SECOND		= 20;
		public static final int TICKS_PER_MINUTE		= TICKS_PER_SECOND * 60;
		public static final int TICKS_PER_HOUR			= TICKS_PER_MINUTE * 60;
		public static final int ENTITY_MAX_AIR			= 300;
		public static final int TICKS_PER_BUBBLE		= ENTITY_MAX_AIR / TICKS_PER_SECOND;
		public static final int TICKS_PER_DAY			= TICKS_PER_SECOND * 1200;
	}
}