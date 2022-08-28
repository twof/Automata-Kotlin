package com.twof.automata

import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.Arm
import net.minecraft.util.Identifier
import java.util.*
import java.util.function.Function

class AutomataEntityModel(root: ModelPart, private val thinArms: Boolean) : BipedEntityModel<AutomataEntity>(root,
    Function { texture: Identifier? ->
        RenderLayer.getEntityTranslucent(
            texture
        )
    }) {
    private val parts: List<ModelPart>
    val leftSleeve: ModelPart
    val rightSleeve: ModelPart
    val leftPants: ModelPart
    val rightPants: ModelPart
    val jacket: ModelPart
    private val cloak: ModelPart
    private val ear: ModelPart

    init {
        ear = root.getChild("ear")
        cloak = root.getChild("cloak")
        leftSleeve = root.getChild("left_sleeve")
        rightSleeve = root.getChild("right_sleeve")
        leftPants = root.getChild("left_pants")
        rightPants = root.getChild("right_pants")
        jacket = root.getChild("jacket")
        parts = root.traverse().filter { part: ModelPart -> !part.isEmpty }
            .collect(ImmutableList.toImmutableList()) as List<ModelPart>
    }

    override fun getBodyParts(): Iterable<ModelPart> {
        return Iterables.concat(
            super.getBodyParts(), ImmutableList.of(
                leftPants, rightPants, leftSleeve, rightSleeve, jacket
            )
        )
    }

    fun renderEars(matrices: MatrixStack?, vertices: VertexConsumer?, light: Int, overlay: Int) {
        ear.copyTransform(head)
        ear.pivotX = 0.0f
        ear.pivotY = 0.0f
        ear.render(matrices, vertices, light, overlay)
    }

    fun renderCape(matrices: MatrixStack?, vertices: VertexConsumer?, light: Int, overlay: Int) {
        cloak.render(matrices, vertices, light, overlay)
    }

    override fun setAngles(livingEntity: AutomataEntity, f: Float, g: Float, h: Float, i: Float, j: Float) {
        super.setAngles(livingEntity, f, g, h, i, j)
        leftPants.copyTransform(leftLeg)
        rightPants.copyTransform(rightLeg)
        leftSleeve.copyTransform(leftArm)
        rightSleeve.copyTransform(rightArm)
        jacket.copyTransform(body)
        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty) {
            if (livingEntity.isInSneakingPose) {
                cloak.pivotZ = 1.4f
                cloak.pivotY = 1.85f
            } else {
                cloak.pivotZ = 0.0f
                cloak.pivotY = 0.0f
            }
        } else if (livingEntity.isInSneakingPose) {
            cloak.pivotZ = 0.3f
            cloak.pivotY = 0.8f
        } else {
            cloak.pivotZ = -1.1f
            cloak.pivotY = -0.85f
        }
    }

    override fun setVisible(visible: Boolean) {
        super.setVisible(visible)
        leftSleeve.visible = visible
        rightSleeve.visible = visible
        leftPants.visible = visible
        rightPants.visible = visible
        jacket.visible = visible
        cloak.visible = visible
        ear.visible = visible
    }

    override fun setArmAngle(arm: Arm, matrices: MatrixStack) {
        val modelPart = getArm(arm)
        if (thinArms) {
            val f = 0.5f * (if (arm == Arm.RIGHT) 1 else -1).toFloat()
            modelPart.pivotX += f
            modelPart.rotate(matrices)
            modelPart.pivotX -= f
        } else {
            modelPart.rotate(matrices)
        }
    }

    fun getRandomPart(random: Random): ModelPart {
        return parts[random.nextInt(parts.size)]
    }

    companion object {
        private const val EAR = "ear"
        private const val CLOAK = "cloak"
        private const val LEFT_SLEEVE = "left_sleeve"
        private const val RIGHT_SLEEVE = "right_sleeve"
        private const val LEFT_PANTS = "left_pants"
        private const val RIGHT_PANTS = "right_pants"

        //        if (slim) {
//            modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
//            modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
//            modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
//            modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
//        } else {
        //        }
        val texturedModelData: TexturedModelData
            get() {
                val dilation = Dilation.NONE
                val modelData = getModelData(dilation, 0.0f)
                val modelPartData = modelData.root
                modelPartData.addChild(
                    "ear",
                    ModelPartBuilder.create().uv(24, 0).cuboid(-3.0f, -6.0f, -1.0f, 6.0f, 6.0f, 1.0f, dilation),
                    ModelTransform.NONE
                )
                modelPartData.addChild(
                    "cloak",
                    ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-5.0f, 0.0f, -1.0f, 10.0f, 16.0f, 1.0f, dilation, 1.0f, 0.5f),
                    ModelTransform.pivot(0.0f, 0.0f, 0.0f)
                )
                val f = 0.25f
                //        if (slim) {
                //            modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
                //            modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
                //            modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
                //            modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
                //        } else {
                modelPartData.addChild(
                    "left_arm",
                    ModelPartBuilder.create().uv(32, 48).cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation),
                    ModelTransform.pivot(5.0f, 2.0f, 0.0f)
                )
                modelPartData.addChild(
                    "left_sleeve",
                    ModelPartBuilder.create().uv(48, 48)
                        .cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)),
                    ModelTransform.pivot(5.0f, 2.0f, 0.0f)
                )
                modelPartData.addChild(
                    "right_sleeve",
                    ModelPartBuilder.create().uv(40, 32)
                        .cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)),
                    ModelTransform.pivot(-5.0f, 2.0f, 0.0f)
                )
                //        }
                modelPartData.addChild(
                    "left_leg",
                    ModelPartBuilder.create().uv(16, 48).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation),
                    ModelTransform.pivot(1.9f, 12.0f, 0.0f)
                )
                modelPartData.addChild(
                    "left_pants",
                    ModelPartBuilder.create().uv(0, 48)
                        .cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)),
                    ModelTransform.pivot(1.9f, 12.0f, 0.0f)
                )
                modelPartData.addChild(
                    "right_pants",
                    ModelPartBuilder.create().uv(0, 32)
                        .cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation.add(0.25f)),
                    ModelTransform.pivot(-1.9f, 12.0f, 0.0f)
                )
                modelPartData.addChild(
                    "jacket",
                    ModelPartBuilder.create().uv(16, 32)
                        .cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, dilation.add(0.25f)),
                    ModelTransform.NONE
                )
                return TexturedModelData.of(modelData, 64, 64)
            }
    }
}