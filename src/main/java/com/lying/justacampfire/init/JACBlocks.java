package com.lying.justacampfire.init;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.lying.justacampfire.block.BlockCampfireBurnt;
import com.lying.justacampfire.block.BlockCampfireLit;
import com.lying.justacampfire.block.BlockCampfireUnlit;
import com.lying.justacampfire.reference.Reference;
import com.lying.justacampfire.tileentity.TileEntityCampfire;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.ModInfo.MOD_ID)
public class JACBlocks
{
	public static final Block CAMPFIRE_UNLIT		= (new BlockCampfireUnlit());
	public static final Block CAMPFIRE_LIT			= (new BlockCampfireLit());
	public static final Block CAMPFIRE_BURNT		= (new BlockCampfireBurnt());
	
	private static final Block[] blocks =
		{
			CAMPFIRE_UNLIT,
			CAMPFIRE_LIT,
			CAMPFIRE_BURNT
		};
	
	private static final ItemBlock[] items =
		{
			(new ItemBlock(CAMPFIRE_LIT)),
			(new ItemBlock(CAMPFIRE_BURNT))
		};
	
	@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID)
	public static class RegistrationHandler
	{
		public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();
		
		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event)
		{
			final IForgeRegistry<Block> registry = event.getRegistry();
			registry.registerAll(blocks);
		}
		
	    @SubscribeEvent
	    public static void registerModels(ModelRegistryEvent event)
	    {
	    	registerBlockItemModel(CAMPFIRE_LIT, "campfire_lit");
	    	registerBlockItemModel(CAMPFIRE_BURNT, "campfire_burnt");
	    }
	    
	    private static void registerBlockItemModel(Block block, int meta, String resourceName)
	    {
			Item blockItem = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(Reference.ModInfo.MOD_PREFIX+resourceName, "inventory"));
	    }
	    private static void registerBlockItemModel(Block block, String resourceName)
	    {
	    	registerBlockItemModel(block, 0, resourceName);
	    }
	    
	    @SuppressWarnings("unused")
		private static void registerRotatedBlockItemModels(Block block, String resourceName)
	    {
	    	registerBlockItemModel(block, resourceName);
			Item blockItem = Item.getItemFromBlock(block);
			for(EnumFacing direction : EnumFacing.HORIZONTALS)
			{
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(Reference.ModInfo.MOD_PREFIX+resourceName, "facing="+direction.toString().toLowerCase()));
			}
	    }

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event)
		{
			final IForgeRegistry<Item> registry = event.getRegistry();
			for (final ItemBlock item : items)
			{
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				item.setRegistryName(registryName);
				item.setCreativeTab(CreativeTabs.DECORATIONS);
				registry.register(item);
				ITEM_BLOCKS.add(item);
			}
			
			registerTileEntities();
		}
	}
	
	private static void registerTileEntities()
	{
		registerTileEntity(TileEntityCampfire.class, "campfire");
	}
	
	@SuppressWarnings("deprecation")
	private static void registerTileEntity(final Class<? extends TileEntity> tileEntityClass, final String name)
	{
		GameRegistry.registerTileEntity(tileEntityClass, Reference.ModInfo.MOD_PREFIX + name);
	}
}
