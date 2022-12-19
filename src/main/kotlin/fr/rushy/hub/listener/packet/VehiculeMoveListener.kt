package fr.rushy.hub.listener.packet

import net.minestom.server.entity.Player
import net.minestom.server.listener.manager.PacketListenerConsumer
import net.minestom.server.network.packet.client.play.ClientVehicleMovePacket

class VehiculeMoveListener : PacketListenerConsumer<ClientVehicleMovePacket> {

    override fun accept(packet: ClientVehicleMovePacket, player: Player) {
        // disabled
    }

}