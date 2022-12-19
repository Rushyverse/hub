package fr.rushy.hub.mount

import net.minestom.server.collision.CollisionUtils
import net.minestom.server.collision.PhysicsResult
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.EntityType
import net.minestom.server.utils.position.PositionUtils

abstract class EntityAbstractMount(type: EntityType) : EntityCreature(type) {

    protected open fun move(time: Long, delta: Vec, physicsResult: PhysicsResult? = null) {
        if (delta.x().isNaN()) {
            return
        }
        val physicsResult = physicsResult ?: CollisionUtils.handlePhysics(this, delta)
        onGround = physicsResult.isOnGround
        val position = physicsResult.newPosition().withView(
            PositionUtils.getLookYaw(delta.x(), delta.z()),
            PositionUtils.getLookPitch(delta.x(), delta.y(), delta.z())
        )
        instance!!.loadChunk(position).thenRun { refreshPosition(position) }
    }
}

abstract class EntityGroundMount(type: EntityType) : EntityAbstractMount(type) {

    override fun move(time: Long, delta: Vec, physicsResult: PhysicsResult?) {
        if (physicsResult != null) {
            super.move(time, delta, physicsResult)
            return
        }

        var physicsResult = CollisionUtils.handlePhysics(this, delta)
        val newDelta = if (!physicsResult.newVelocity.samePoint(delta) && isOnGround) {
            val newDelta = delta.withY { it + 1 }
            physicsResult = CollisionUtils.handlePhysics(this, newDelta)
            newDelta
        } else {
            delta
        }

        super.move(time, newDelta, physicsResult)
    }

}