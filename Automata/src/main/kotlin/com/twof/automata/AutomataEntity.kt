package com.twof.automata

import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.world.World

class AutomataEntity(entityType: EntityType<out PathAwareEntity>?, world: World?) : PathAwareEntity(entityType,
    world
) {

    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder {
            return PathAwareEntity.createMobAttributes()
        }
    }
    public override fun jump() {
        super.jump()
    }
}