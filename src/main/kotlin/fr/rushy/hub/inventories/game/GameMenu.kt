package fr.rushy.hub.inventories.game

import fr.rushy.hub.inventories.MainMenu
import fr.rushy.invext.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.Tag

class GameMenu(val player: Player, val game: String) {

    fun get(): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, "Menu du jeu $game")


        val submitProposalItem = getSubmitProposalItem()
        inventory.setClickableItem(6, submitProposalItem, Clickable {
            player.openBook(SubmitProposalMenu(player, game).get())
        });

        val playNow = getPlayNowItem()
        inventory.setClickableItem(4, playNow, Clickable {
            player.sendMessage(Component.text("Ajouté à la file d'attente: $game", NamedTextColor.GREEN))
        })

        val leaderboardItem = getLeaderboardItem()
        inventory.setClickableItem(2, leaderboardItem, Clickable {
            player.openInventory(LeaderboardMenu(player, game).get())
        })

        inventory.setCloseButton(42)
        inventory.setBackButton(40, MainMenu(player).get())
        return inventory
    }

    private fun getLeaderboardItem(): ItemStack {
        return ItemStack.of(Material.GOLDEN_SWORD)
            .withDisplayName(
                Component.text("Classement", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
            )
            .withLore("Voir le classement")
            .withTag(Tag.Integer("HideFlags"), 32)
    }

    private fun getPlayNowItem(): ItemStack = ItemStack.of(Material.ENDER_PEARL)
        .withDisplayName(
            Component.text("Jouer !").color(NamedTextColor.LIGHT_PURPLE)
        ).withLore("Cliquez pour rejoindre une\npartie dès maintenant !")

    private fun getSubmitProposalItem(): ItemStack {
        val item = ItemStack.of(Material.PAPER)
            .withDisplayName(
                Component.text("Proposer une idée").color(NamedTextColor.GREEN)
            ).withLore("Cliquez pour proposer une idée\nconcernant ce jeu")
        return item;
    }
}