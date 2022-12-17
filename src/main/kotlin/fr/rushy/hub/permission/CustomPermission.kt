package fr.rushy.hub.permission

/**
 * Custom permission of the server.
 */
interface CustomPermission {
    companion object {
        const val GAMEMODE: String = "custom.gamemode"
        const val KICK: String = "custom.kick"
        const val STOP_SERVER: String = "custom.stop"
        const val GIVE: String = "custom.give"
    }
}