package com.twof.automata.actions

import com.twof.automata.AutomataEntity

interface EntityAction {
    fun performAction(automataEntity: AutomataEntity)
}

class JumpAction: EntityAction {
    override fun performAction(automataEntity: AutomataEntity) {
        automataEntity.jump()
    }
}