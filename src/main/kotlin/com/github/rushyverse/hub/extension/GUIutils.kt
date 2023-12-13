package com.github.rushyverse.hub.extension

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.hub.Hub
import org.bukkit.Material
import java.util.*

object GUIUtils {

    val translator: Translator by inject(Hub.ID)

    fun createBackward(locale: Locale) = ItemStack(
        type = Material.ARROW,
        name = "shop.backwards.name",
        description = "shop.backwards.desc",
        locale = locale,
        translator = translator
    )

    fun createUnequip(locale: Locale) = ItemStack(
        type = Material.BARRIER,
        name = "shop.unequipitem.name",
        description = "shop.unequipitem.desc",
        locale = locale,
        translator = translator
    )
}