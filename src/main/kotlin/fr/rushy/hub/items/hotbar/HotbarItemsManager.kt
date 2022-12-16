package fr.rushy.hub.items.hotbar

import fr.rushy.hub.items.CustomItem
import net.minestom.server.item.Material

class HotbarItemsManager private constructor() {

    companion object {
        val customItemMap: HashMap<Material, CustomItem> = HashMap()

        private val mainMenuItem: MainMenuItem = registerItem(MainMenuItem())
        private val statsMenuItem: StatsMenuItem = registerItem(StatsMenuItem())
        private val cosmeticsMenuItem: CosmeticsMenuItem = registerItem(CosmeticsMenuItem())

        fun getItems(): List<CustomItem> {
            return customItemMap.values.toList()
        }

        fun getMainMenuItem(): MainMenuItem {
            return mainMenuItem
        }

        fun getStatsMenuItem(): StatsMenuItem {
            return statsMenuItem
        }

        fun getCosmeticsMenuItem(): CosmeticsMenuItem {
            return cosmeticsMenuItem
        }

        /**
         * Get a registered CustomItem from the Material.
         * It is important to note that each Material can only be associated with one CustomItem for more coherence.
         */
        fun getRegisteredFromMaterial(material: Material): CustomItem? {
            return customItemMap[material]
        }

        private fun <T : CustomItem> registerItem(item: T): T {
            customItemMap[item.getMaterial()] = item
            return item
        }
    }
}