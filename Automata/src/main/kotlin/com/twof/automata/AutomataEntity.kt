package com.twof.automata

import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.UUID
import kotlin.math.abs

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

    fun uuid(): UUID {
        return super.uuid
    }

    fun localVisableBlocks(distance: Double): MutableSet<BlockEntity> {
        var automataPosition = this.pos
        var visited: MutableSet<Vec3d> = mutableSetOf()
        var visibleBlocks: MutableSet<BlockEntity> = mutableSetOf()
        var stack: MutableList<Vec3d> = mutableListOf(automataPosition)
        var translations: List<Double> = listOf(-1.0, 0.0, 1.0)
        // flood-fill to look for non-air blocks immediately accessible to the automaton
        while (true) {
            val pos = stack.removeLastOrNull() ?: break
            visited.add(pos)
            val blockAtPos = world.getBlockEntity(BlockPos(pos))?.type
            print(blockAtPos)

            for (x in translations) {
                for (y in translations) {
                    for (z in translations) {
                        val translatedPos = Vec3d(pos.x + x, pos.y + y, pos.z + z)
                        val distanceX = abs(automataPosition.x - translatedPos.x)
                        val distanceY = abs(automataPosition.y - translatedPos.y)
                        val distanceZ = abs(automataPosition.z - translatedPos.z)
                        val maxDistance = listOf<Double>(distanceX, distanceY, distanceZ).max()
                        if (!visited.contains(translatedPos) && maxDistance < distance) {
                            stack.add(translatedPos)
                        }
                    }
                }
            }
        }
        return visibleBlocks
    }
}