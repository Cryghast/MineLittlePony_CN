package com.minelittlepony.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

import org.jetbrains.annotations.Nullable;

import com.minelittlepony.api.events.PonyModelPrepareCallback;
import com.minelittlepony.api.model.*;
import com.minelittlepony.api.pony.Pony;
import com.minelittlepony.api.pony.PonyData;
import com.minelittlepony.api.pony.meta.Size;
import com.minelittlepony.api.pony.meta.SizePreset;
import com.minelittlepony.mson.api.model.biped.MsonPlayer;

/**
 * The raw pony model without any implementations.
 * Will act effectively the same as a normal player model without any hints
 * of being cute and adorable.
 *
 * Modders can extend this class to make their own pony models if they wish.
 */
public abstract class ClientPonyModel<T extends LivingEntity> extends MsonPlayer<T> implements PonyModel<T> {

    /**
     * The model attributes.
     */
    protected ModelAttributes attributes = new ModelAttributes();

    @Nullable
    protected PosingCallback<T> onSetModelAngles;

    public ClientPonyModel(ModelPart tree) {
        super(tree);
    }

    public void onSetModelAngles(PosingCallback<T> callback) {
        onSetModelAngles = callback;
    }

    protected Arm getPreferredArm(T livingEntity) {
        Arm arm = livingEntity.getMainArm();
        return livingEntity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
    }

    @Override
    public void updateLivingState(T entity, Pony pony, ModelAttributes.Mode mode) {
        child = entity.isBaby();
        attributes.updateLivingState(entity, pony, mode);
        PonyModelPrepareCallback.EVENT.invoker().onPonyModelPrepared(entity, this, mode);
        sneaking = attributes.isCrouching;
        riding = attributes.isSitting;
    }

    @Override
    public final void copyAttributes(BipedEntityModel<T> other) {
        copyStateTo(other);
    }

    /**
     * Copies this model's attributes into the passed model.
     */
    @Override
    public void copyStateTo(EntityModel<T> model) {
        super.copyStateTo(model);

        if (model instanceof ClientPonyModel) {
            ((ClientPonyModel<T>)model).attributes = attributes;
        }
    }

    @Override
    public final ModelAttributes getAttributes() {
        return attributes;
    }

    @Override
    public Size getSize() {
        return child ? SizePreset.FOAL : PonyModel.super.getSize();
    }

    @Override
    public void setMetadata(PonyData meta) {
        attributes.metadata = meta;
    }

    @Override
    public float getSwingAmount() {
        return handSwingProgress;
    }

    @Override
    public ModelPart getForeLeg(Arm side) {
        return getArm(side);
    }

    @Override
    public ModelPart getHindLeg(Arm side) {
        return side == Arm.LEFT ? leftLeg : rightLeg;
    }

    @Override
    public ArmPose getArmPoseForSide(Arm side) {
        return side == Arm.RIGHT ? rightArmPose : leftArmPose;
    }

    @Override
    public void setHatVisible(boolean visible) {

    }

    static void resetPivot(ModelPart part) {
        part.setPivot(part.getDefaultTransform().pivotX, part.getDefaultTransform().pivotY, part.getDefaultTransform().pivotZ);
    }

    static void resetPivot(ModelPart...parts) {
        for (ModelPart part : parts) {
            resetPivot(part);
        }
    }

    public interface PosingCallback<T extends LivingEntity> {
        void poseModel(ClientPonyModel<T> model, float move, float swing, float ticks, T entity);
    }
}
