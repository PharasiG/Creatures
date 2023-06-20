package com.raywenderlich.android.creatures.model

class CompositeItem {

    var creature: Creature? = null
        private set

    var header: Header? = null
        private set

    var isHeader = false
        private set

    companion object {
        fun withCreature(creature: Creature): CompositeItem {
            val composite = CompositeItem()
            composite.creature = creature
            return composite
        }

        fun withHeader(header: Header): CompositeItem {
            val composite = CompositeItem()
            composite.header = header
            composite.isHeader = true
            return composite
        }
    }

    override fun toString(): String {
        return if (isHeader) header?.name ?: "Header was actually null"
        else creature?.nickname ?: "Creature was actually null"
    }
}