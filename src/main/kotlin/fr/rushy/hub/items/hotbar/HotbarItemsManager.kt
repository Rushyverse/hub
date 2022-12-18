package fr.rushy.hub.items.hotbar

import fr.rushy.hub.items.Clickable
import fr.rushy.hub.items.CustomItem
import fr.rushy.hub.items.hotbar.top.LangMenuItem
import fr.rushy.hub.items.hotbar.top.ParametersMenuItem
import fr.rushy.hub.items.hotbar.top.SocialMenuItem
import net.minestom.server.item.Material

object HotbarItemsManager {

    val customItemMap: MutableMap<Material, Clickable> = mutableMapOf()

    val mainMenuItem: MainMenuItem = registerClickableOfItem(MainMenuItem())
    val statsMenuItem: StatsMenuItem = registerClickableOfItem(StatsMenuItem())
    val cosmeticsMenuItem: CosmeticsMenuItem = registerClickableOfItem(CosmeticsMenuItem())
    val langMenuItem: LangMenuItem = registerClickableOfItem(LangMenuItem())
    val parametersMenuItem: ParametersMenuItem = registerClickableOfItem(ParametersMenuItem())
    val socialMenuItem: SocialMenuItem = registerClickableOfItem(SocialMenuItem())

    /**
     * Get a registered Clickable from the Material.
     * It is important to note that each Material can only be associated with one Clickable for more coherence.
     */
    fun getClickableFromMaterial(material: Material): Clickable? {
        return customItemMap[material]
    }

    private fun <T : CustomItem> registerClickableOfItem(item: T): T {
        customItemMap[item.material] = item
        return item
    }
}