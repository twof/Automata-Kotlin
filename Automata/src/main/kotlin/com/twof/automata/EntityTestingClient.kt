package com.twof.automata

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class EntityTestingClient : ClientModInitializer {
    override fun onInitializeClient() {
        // In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
        EntityRendererRegistry.register(
            Automata.automataEntity
        ) { context ->
            AutomataEntityRenderer(context!!)
        }
        EntityModelLayerRegistry.registerModelLayer(MODEL_AUTOMATA_LAYER) { AutomataEntityModel.texturedModelData }
    }

    companion object {
        val MODEL_AUTOMATA_LAYER = EntityModelLayer(Identifier("entitytesting", "automata"), "main")
    }
}