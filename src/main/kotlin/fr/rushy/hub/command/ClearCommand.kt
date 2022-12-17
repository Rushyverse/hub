package fr.rushy.hub.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity
import net.minestom.server.entity.Player

class ClearCommand : Command("clear") {

    init {
        val playerArg = argumentPlayer()

        setCondition { sender, _ ->
            sender !is Player || hasPermission(sender)
        }

        setDefaultExecutor { sender, context ->
            if(sender !is Player) {
                sender.sendMessage("Tu n'es pas un joueur")
                return@setDefaultExecutor
            }

            sender.getAcquirable<Player>().sync {
                it.inventory.clear()
            }
            sender.sendMessage("Votre inventaire a été vidé.")
        }

        addExecutor(playerArg)
    }

    /**
     * Define the syntax to process the command on another player.
     * @param playerArg Argument to retrieve player(s) selected.
     */
    private fun addExecutor(playerArg: ArgumentEntity) {
        addSyntax({ sender, context ->
            val player = context.get(playerArg).find(sender).asSequence().filterIsInstance<Player>().firstOrNull()
            if (player == null) {
                sendPlayerNotFoundMessage(sender)
                return@addSyntax
            }

            player.getAcquirable<Player>().sync {
                it.inventory.clear()
            }

            player.sendMessage("Votre inventaire a été vidé.")

            if(player != sender) {
                sender.sendMessage("L'inventaire de ${player.name} a été vidé.")
            }
        }, playerArg)
    }

    /**
     * Check if the player has the permission to execute the command
     * @param sender Player.
     * @return `true` if the player is authorized to execute, `false` otherwise.
     */
    private fun hasPermission(sender: Player) = true

    /**
     * Create a new argument targeting players name.
     * @return New argument.
     */
    private fun argumentPlayer(): ArgumentEntity =
        ArgumentType.Entity("target").onlyPlayers(true)
}