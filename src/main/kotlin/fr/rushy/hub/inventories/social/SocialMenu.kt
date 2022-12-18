package fr.rushy.hub.inventories.social

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

class SocialMenu(val player: Player) : Inventory(InventoryType.CHEST_1_ROW, "Social") {

    init {

        val friendsItem = generateFriendsItem()
        setClickableItem(0, friendsItem, object : Clickable {
            override fun onClick(player: Player) {
                player.openInventory(FriendsMenu(player))
            }
        })

        val guildsItem = generateGuildsItem()
        setClickableItem(1, guildsItem, object : Clickable {
            override fun onClick(player: Player) {
                player.openInventory(GuildMenu(player))
            }
        })

        setCloseButton(8)
    }

    private fun generateGuildsItem(): ItemStack = createItem(
        Material.CLOCK,
        "Guildes",
        "Ouvrir le menu relatif aux guildes.\nClassements, invitations, etc."
    )

    private fun generateFriendsItem(): ItemStack = createItem(
        Material.WRITTEN_BOOK,
        "Amis",
        "Ajoutez, chattez et rejoignez\nvos amis à travers le serveur"
    ).withTag(Tag.Integer("HideFlags"), 32)

    private fun createItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.of(material, 1)
            .withDisplayName(Component.text(name).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
            .withLore(description)
    }

}