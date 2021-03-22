package com.lying.justacampfire.client.renderer.tileentity;

import com.lying.justacampfire.tileentity.TileEntityCampfire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityCampfireRenderer extends TileEntitySpecialRenderer<TileEntityCampfire>
{
	private static final double itemSpread = 0.35D;
	
    public void render(TileEntityCampfire theTile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    	
    	GlStateManager.pushMatrix();
	    	GlStateManager.translate(x + 0.5D, y + 0.4375D, z + 0.5D);
	    	for(int slot=0; slot<4; slot++)
	    	{
	    		GlStateManager.pushMatrix();
	    		GlStateManager.translate(itemSpread * (slot%2 == 0 ? -1 : 1), 0D, itemSpread * (slot > 1 ? 1 : -1));
	    		
	    		ItemStack stackInSlot = theTile.getContentsOfSlot(slot);
	    		if(!stackInSlot.isEmpty())
    			{
	    	    	GlStateManager.rotate(90F, 1F, 0F, 0F);
	    	    	double rescale = 0.3D;
	    	    	GlStateManager.scale(rescale, rescale, rescale);
	    			renderItem.renderItem(stackInSlot, TransformType.FIXED);
    			}
	    		GlStateManager.popMatrix();
	    	}
    	GlStateManager.popMatrix();
    }
}
