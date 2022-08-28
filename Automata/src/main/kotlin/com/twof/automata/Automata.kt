package com.twof.automata

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.util.registry.Registry
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.EntityDimensions
import net.minecraft.item.SpawnEggItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier

@Suppress("UNUSED")
object Automata: ModInitializer {
    private const val MOD_ID = "automata"
    val automataEntity = Registry.register(
        Registry.ENTITY_TYPE,
        Identifier("automata", "automata"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ::AutomataEntity)
            .dimensions(EntityDimensions.fixed(1f, 2f)).build()
    )

    override fun onInitialize() {

        FabricDefaultAttributeRegistry.register(automataEntity, AutomataEntity.createMobAttributes())

        val automataSpawnEgg = SpawnEggItem(automataEntity, 12895428, 11382189, Item.Settings().group(ItemGroup.MISC))
        Registry.register(Registry.ITEM, Identifier("automata", "automata_spawn_egg"), automataSpawnEgg)
        println("Example mod has been initialized.")
    }
}