package fr.rushy.hub.mount

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import net.minestom.server.entity.vehicle.PlayerVehicleInformation

class PigMount : EntityGroundMount(EntityType.PIG) {

    override fun update(time: Long) {
        super.update(time)
        val passenger = passengers.firstOrNull() ?: return
        val player = passenger as? Player ?: return
        val vehiculeInformation = player.vehicleInformation

        if (vehiculeInformation.shouldUnmount()) {
            remove()
            return
        }

        if (!wantsMove(vehiculeInformation)) {
            if (hasNoGravity()) {
                move(time, Vec(0.0, -gravityAcceleration, 0.0))
            }
            return
        }

        val delta = Vec(
            vehiculeInformation.forward.coerceIn(-1F, 1F).toDouble(),
            0.0,
            -vehiculeInformation.sideways.coerceIn(-1F, 1F).toDouble()
        ).rotateFromView(player.position)
            .withY(0.0)
            .normalize()
            .mul(0.4)
//            .withY(if (vehiculeInformation.shouldJump() && isOnGround) gravityAcceleration * 10 else 0.0)
        if (delta.x().isNaN()) {
            return
        }
        move(time, delta)
    }

    private fun wantsMove(vehiculeInformation: PlayerVehicleInformation) =
        vehiculeInformation.shouldJump() || vehiculeInformation.forward != 0F || vehiculeInformation.sideways != 0F
}