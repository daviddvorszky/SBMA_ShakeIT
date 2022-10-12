package org.sbma_shakeit.data.room

import androidx.room.Embedded
import androidx.room.Relation

class UserWithShakes (
    @Embedded val user: User,
    @Relation(
        parentColumn = "username",
        entityColumn = "parent_username"
    )
    val shakes: List<Shake>? = null
)