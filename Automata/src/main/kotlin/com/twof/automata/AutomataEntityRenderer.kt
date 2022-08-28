package com.twof.automata

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.*
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.CrossbowItem
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.UseAction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f

class AutomataEntityRenderer(ctx: EntityRendererFactory.Context) :
    LivingEntityRenderer<AutomataEntity, AutomataEntityModel?>(
        ctx,
        AutomataEntityModel(ctx.getPart(EntityModelLayers.PLAYER), false),
        0.5f
    ) {
    init {
//        addFeature(
//            ArmorFeatureRenderer<Any?, Any?, Any?>(
//                this,
//                BipedEntityModel(ctx.getPart(if (slim) EntityModelLayers.PLAYER_SLIM_INNER_ARMOR else EntityModelLayers.PLAYER_INNER_ARMOR)),
//                BipedEntityModel(ctx.getPart(if (slim) EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR else EntityModelLayers.PLAYER_OUTER_ARMOR))
//            )
//        )

//        addFeature(PlayerHeldItemFeatureRenderer(this, ctx.heldItemRenderer))
//        addFeature(StuckArrowsFeatureRenderer(ctx, this))
//        addFeature(HeadFeatureRenderer(this, ctx.modelLoader, ctx.heldItemRenderer))
//        addFeature(ElytraFeatureRenderer<Any?, Any?>(this, ctx.modelLoader))
//        addFeature(TridentRiptideFeatureRenderer<Any?>(this, ctx.modelLoader))
//        addFeature(StuckStingersFeatureRenderer<Any?, Any?>(this))
    }

    override fun render(
        automataEntity: AutomataEntity,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int
    ) {
        setModelPose(automataEntity)
        super.render(automataEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }

    override fun getPositionOffset(automataEntity: AutomataEntity, f: Float): Vec3d {
        return if (automataEntity.isInSneakingPose) Vec3d(0.0, -0.125, 0.0) else super.getPositionOffset(
            automataEntity,
            f
        )
    }

    private fun setModelPose(player: AutomataEntity) {
        val playerEntityModel = getModel()
        if (player.isSpectator) {
            playerEntityModel!!.setVisible(false)
            playerEntityModel.head.visible = true
            playerEntityModel.hat.visible = true
        } else {
            playerEntityModel!!.setVisible(true)
            //            playerEntityModel.hat.visible = player.isPartVisible(PlayerModelPart.HAT);
//            playerEntityModel.jacket.visible = player.isPartVisible(PlayerModelPart.JACKET);
//            playerEntityModel.leftPants.visible = player.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
//            playerEntityModel.rightPants.visible = player.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
//            playerEntityModel.leftSleeve.visible = player.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
//            playerEntityModel.rightSleeve.visible = player.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
            playerEntityModel.sneaking = player.isInSneakingPose
            val armPose = getArmPose(player, Hand.MAIN_HAND)
            var armPose2 = getArmPose(player, Hand.OFF_HAND)
            if (armPose.isTwoHanded) {
                armPose2 = if (player.offHandStack.isEmpty) ArmPose.EMPTY else ArmPose.ITEM
            }
            if (player.mainArm == Arm.RIGHT) {
                playerEntityModel.rightArmPose = armPose
                playerEntityModel.leftArmPose = armPose2
            } else {
                playerEntityModel.rightArmPose = armPose2
                playerEntityModel.leftArmPose = armPose
            }
        }
    }

    override fun getTexture(automataEntity: AutomataEntity): Identifier {
        return Identifier("textures/entity/steve.png")
    }

    override fun scale(automataEntity: AutomataEntity, matrixStack: MatrixStack, f: Float) {
        val g = 0.9375f
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f)
    }

    override fun renderLabelIfPresent(
        automataEntity: AutomataEntity,
        text: Text,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int
    ) {
//        double d = this.dispatcher.getSquaredDistanceToCamera(automataEntity);
        matrixStack.push()
        //        if (d < 100.0D) {
//            Scoreboard scoreboard = automataEntity.getScoreboard();
//            ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(2);
//            if (scoreboardObjective != null) {
//                ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(automataEntity.getEntityName(), scoreboardObjective);
//                super.renderLabelIfPresent(automataEntity, (new LiteralText(Integer.toString(scoreboardPlayerScore.getScore()))).append(" ").append(scoreboardObjective.getDisplayName()), matrixStack, vertexConsumerProvider, i);
//                Objects.requireNonNull(this.getTextRenderer());
//                matrixStack.translate(0.0D, (double)(9.0F * 1.15F * 0.025F), 0.0D);
//            }
//        }
        super.renderLabelIfPresent(automataEntity, text, matrixStack, vertexConsumerProvider, i)
        matrixStack.pop()
    }

    fun renderRightArm(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        player: AutomataEntity
    ) {
        renderArm(matrices, vertexConsumers, light, player, model!!.rightArm, model!!.rightSleeve)
    }

    fun renderLeftArm(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        player: AutomataEntity
    ) {
        renderArm(matrices, vertexConsumers, light, player, model!!.leftArm, model!!.leftSleeve)
    }

    private fun renderArm(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        player: AutomataEntity,
        arm: ModelPart,
        sleeve: ModelPart
    ) {
        val playerEntityModel = getModel()
        setModelPose(player)
        playerEntityModel!!.handSwingProgress = 0.0f
        playerEntityModel.sneaking = false
        playerEntityModel.leaningPitch = 0.0f
        playerEntityModel.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        arm.pitch = 0.0f
        arm.render(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier("textures/entity/steve.png"))),
            light,
            OverlayTexture.DEFAULT_UV
        )
        sleeve.pitch = 0.0f
        sleeve.render(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(Identifier("textures/entity/steve.png"))),
            light,
            OverlayTexture.DEFAULT_UV
        )
    }

    override fun setupTransforms(
        automataEntity: AutomataEntity,
        matrixStack: MatrixStack,
        f: Float,
        g: Float,
        h: Float
    ) {
        val i = automataEntity.getLeaningPitch(h)
        val n: Float
        val k: Float
        if (automataEntity.isFallFlying) {
            super.setupTransforms(automataEntity, matrixStack, f, g, h)
            n = automataEntity.roll.toFloat() + h
            k = MathHelper.clamp(n * n / 100.0f, 0.0f, 1.0f)
            if (!automataEntity.isUsingRiptide) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k * (-90.0f - automataEntity.pitch)))
            }
            val vec3d = automataEntity.getRotationVec(h)
            val vec3d2 = automataEntity.velocity
            val d = vec3d2.horizontalLengthSquared()
            val e = vec3d.horizontalLengthSquared()
            if (d > 0.0 && e > 0.0) {
                val l = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e)
                val m = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x
                matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((Math.signum(m) * Math.acos(l)).toFloat()))
            }
        } else if (i > 0.0f) {
            super.setupTransforms(automataEntity, matrixStack, f, g, h)
            n = if (automataEntity.isTouchingWater) -90.0f - automataEntity.pitch else -90.0f
            k = MathHelper.lerp(i, 0.0f, n)
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k))
            if (automataEntity.isInSwimmingPose) {
                matrixStack.translate(0.0, -1.0, 0.30000001192092896)
            }
        } else {
            super.setupTransforms(automataEntity, matrixStack, f, g, h)
        }
    }

    companion object {
        private fun getArmPose(player: AutomataEntity, hand: Hand): ArmPose {
            val itemStack = player.getStackInHand(hand)
            return if (itemStack.isEmpty) {
                ArmPose.EMPTY
            } else {
                if (player.activeHand == hand && player.itemUseTimeLeft > 0) {
                    val useAction = itemStack.useAction
                    if (useAction == UseAction.BLOCK) {
                        return ArmPose.BLOCK
                    }
                    if (useAction == UseAction.BOW) {
                        return ArmPose.BOW_AND_ARROW
                    }
                    if (useAction == UseAction.SPEAR) {
                        return ArmPose.THROW_SPEAR
                    }
                    if (useAction == UseAction.CROSSBOW && hand == player.activeHand) {
                        return ArmPose.CROSSBOW_CHARGE
                    }
                    if (useAction == UseAction.SPYGLASS) {
                        return ArmPose.SPYGLASS
                    }
                } else if (!player.handSwinging && itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
                    return ArmPose.CROSSBOW_HOLD
                }
                ArmPose.ITEM
            }
        }
    }
}