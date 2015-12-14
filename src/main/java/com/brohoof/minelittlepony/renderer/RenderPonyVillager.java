 package com.brohoof.minelittlepony.renderer;

import com.brohoof.minelittlepony.MineLittlePony;
import com.brohoof.minelittlepony.Pony;
import com.brohoof.minelittlepony.model.PMAPI;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

public class RenderPonyVillager extends RenderPonyMob<EntityVillager> {

    public RenderPonyVillager(RenderManager rm) {
        super(rm, PMAPI.pony);
    }

    @Override
    protected void preRenderCallback(EntityVillager villager, float partialTicks) {
        if (villager.getGrowingAge() < 0) {
            this.shadowSize = 0.25F;
        } else {
            if (MineLittlePony.getConfig().getShowScale().get()) {
                this.shadowSize = 0.4F;
            } else {
                this.shadowSize = 0.5F;
            }
        }

        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityVillager villager) {
        Pony aVillagerPony = MineLittlePony.getInstance().getManager().getPonyFromResourceRegistry(villager);
        return aVillagerPony.getTextureResourceLocation();
    }
}
