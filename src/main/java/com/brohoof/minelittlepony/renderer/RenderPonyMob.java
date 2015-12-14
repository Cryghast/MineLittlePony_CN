package com.brohoof.minelittlepony.renderer;

import com.brohoof.minelittlepony.MineLittlePony;
import com.brohoof.minelittlepony.PonyGender;
import com.brohoof.minelittlepony.PonyRace;
import com.brohoof.minelittlepony.TailLengths;
import com.brohoof.minelittlepony.model.ModelPony;
import com.brohoof.minelittlepony.model.PlayerModel;
import com.brohoof.minelittlepony.renderer.layer.LayerHeldPonyItem;
import com.brohoof.minelittlepony.renderer.layer.LayerPonyArmor;
import com.brohoof.minelittlepony.renderer.layer.LayerPonySkull;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class RenderPonyMob<T extends EntityLiving> extends RenderLiving implements IRenderPony {
    protected ModelPony mobModel;
    protected PlayerModel playerModel;

    public RenderPonyMob(RenderManager renderManager, PlayerModel playerModel) {
        super(renderManager, playerModel.getModel(), playerModel.getShadowsize());
        this.mobModel = playerModel.getModel();
        this.playerModel = playerModel;

        this.addLayer(new LayerPonyArmor(this));
        this.addLayer(new LayerHeldPonyItem(this));
        // this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerPonySkull(this));
    }

    @Override
    public void doRender(EntityLiving entity, double xPosition, double yPosition, double zPosition, float yaw,
            float partialTicks) {
        ItemStack heldItem = entity.getHeldItem();
        this.playerModel.getModel().heldItemRight = heldItem == null ? 0 : 1;

        this.playerModel.getModel().isSneak = false;
        this.playerModel.getModel().isFlying = false;

        if (entity instanceof EntitySkeleton) {

            switch (entity.getUniqueID().hashCode() % 3) {
            case 0:
            case 1:
                this.playerModel.getModel().metadata.setRace(PonyRace.UNICORN);
                break;
            case 2:
                this.playerModel.getModel().metadata.setRace(PonyRace.EARTH);
            }
        } else {
            this.playerModel.getModel().metadata.setRace(PonyRace.EARTH);
        }

        if (entity instanceof EntityPigZombie) {
            this.playerModel.getModel().metadata.setGender(PonyGender.STALLION);
        } else {
            this.playerModel.getModel().metadata.setGender(PonyGender.MARE);
        }

        if (entity instanceof EntitySkeleton) {
            this.playerModel.getModel().metadata.setTail(TailLengths.STUB);
        } else {
            this.playerModel.getModel().metadata.setTail(TailLengths.FULL);
        }

        this.playerModel.getModel().isSleeping = false;
        if (MineLittlePony.getConfig().getShowScale().get()) {
            this.shadowSize = 0.4F;
        }

        double yOrigin = yPosition;
        if (entity.isSneaking()) {
            yOrigin -= 0.125D;
        }

        super.doRender(entity, xPosition, yOrigin, zPosition, yaw, partialTicks);
        this.playerModel.getModel().aimedBow = false;
        this.playerModel.getModel().isSneak = false;
        this.playerModel.getModel().heldItemRight = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doRender(Entity entity, double xPosition, double yPosition, double zPosition, float yaw,
            float partialTicks) {
        this.doRender((T) entity, xPosition, yPosition, zPosition, yaw, partialTicks);
    }

    protected abstract ResourceLocation getEntityTexture(T var1);

    @SuppressWarnings("unchecked")
    @Override
    protected ResourceLocation getEntityTexture(Entity var1) {
        return this.getEntityTexture((T) var1);
    }

    protected void preRenderCallback(T entity, float partick) {}

    @SuppressWarnings("unchecked")
    @Override
    protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime) {
        preRenderCallback((T) entitylivingbaseIn, partialTickTime);
    }

    protected void rotateCorpse(T entity, float xPosition, float yPosition, float zPosition) {
        super.rotateCorpse(entity, xPosition, yPosition, zPosition);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void rotateCorpse(EntityLivingBase entity, float xPosition, float yPosition, float zPosition) {
        this.rotateCorpse((T) entity, xPosition, yPosition, zPosition);
    }

    @Override
    public PlayerModel getPony() {
        return playerModel;
    }
}
