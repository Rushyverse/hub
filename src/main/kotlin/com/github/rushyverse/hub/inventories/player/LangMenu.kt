package com.github.rushyverse.hub.inventories.player

import com.github.rushyverse.api.extension.*
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta
import net.minestom.server.tag.Tag
import java.util.*

class LangMenu(private val translationsProvider: TranslationsProvider, private val locale: Locale, val player: Player) :
    IMenu {

    override suspend fun build(): Inventory {
        val inv = Inventory(
            InventoryType.CHEST_1_ROW,
            translationsProvider.translate("lang_menu_title", locale, BUNDLE_HUB)
        )

        sequenceOf(createFrenchItem(), createEnglishItem(), createSpanishItem())
            .forEach {item ->
                inv.addItemStackSuspend(item) { player, _, _, _ -> run {
                        val lang: String = item.getTag(Tag.String("lang"))
                        player.sendMessage("New language: $lang")
                        // TODO: Execute the lang command for the player in the proxy
                        // MinecraftServer.getCommandManager().execute(player, "lang $lang")
                    }
                }
            }

        inv.setCloseButton(8)
        return inv
    }

    private fun createFrenchItem(): ItemStack {
        return buildLangItem(
            "Français",
            "Parlez-vous français ?",
            "fr",
            PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY2NDMxMTQ2NzYxMywKICAicHJvZmlsZUlkIiA6ICJmODJmNTQ1MDIzZDA0MTFkYmVlYzU4YWI4Y2JlMTNjNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZXNwb25kZW50cyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82OTAzMzQ5ZmE0NWJkZDg3MTI2ZDljZDNjNmMwYWJiYTdkYmQ2ZjU2ZmI4ZDc4NzAxODczYTFlN2M4ZWUzM2NmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "CMjXbLXVEwcy0ysuH1oiwtwSU+QvN3E5AO7MO0gKIMSLdtvGH3HVkAZQA03laQ1gUAFzOlrtwnhXEUijAJZOz3oyVNKdG8ANlzL+0bG1jHhWIjxpA5A7XslJaXLsch+R9fxJYyfisBvBp+/KlMb+gAwFGxa2LYkeQ2P3DdtQC85MWdZcvqJvoaYxLoZtF6eimlInMGPwftLhchbKrgA6EFk1pbJ0nP7+MQaLXuhWofICj0OXyuYmntTv+qv44cW+Ymzp4y4NtehFH8Bai9sbta5GXzR2Vgk4M3+ETb4W9lYMSKHVe+puh35lFnCjRrwUD1JEXqLlT6agFvnbqPz4nGJoMQ34PPrq2RGNHf4AKBYycN6RAoG0br5txrJovtpKtu65FqvgCaLiF1vRsbWCTdZtXktl8rd0V+01Q2ECnKi82kQhfU30ooKi73fBDpNR1vuzxa2baOM8l/kwWF95iH0STWtkZ5fd0mE/wBXdlVh+IGJ1HdL1l8Non6VgIvppIg3D2XcPRr5Qff1gOf1S1a38BgGhMmOJkg+/td1k+74a3TYzjv1xwLxLz4WKnNJFSJavSNk0XL1hT0iElTrsrXz7Un079VFJ0etpiSGhpGs5rJSlb6f4bBTwRZuTwVOYHGm7CfAU2dbFMtomwxSzNe0IgdXmWpQIB4+FYo+/5ME="
            )
        )
    }

    private fun createEnglishItem(): ItemStack {
        return buildLangItem(
            "English",
            "Do you speak english?",
            "en",
            PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY2Mzc5MTcyMzI0NiwKICAicHJvZmlsZUlkIiA6ICI0NDAzZGM1NDc1YmM0YjE1YTU0OGNmZGE2YjBlYjdkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHYXl0b3duIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg4MzFjNzNmNTQ2OGU4ODhjMzAxOWUyODQ3ZTQ0MmRmYWE4ODg5OGQ1MGNjZjAxZmQyZjkxNGFmNTQ0ZDUzNjgiCiAgICB9CiAgfQp9",
                "TiEYGQxEC+iLBk0QNFCXXa/7o+6Teo/GhpjTIQKLI16OpBAP9gZ1b5mJWfN31X2jZCIM/m+o4mdDP2Lafuyt9Fw60MQOdBFDPGuybQlm7Y/UUlfy9T2HWRDbF9pE0B+E6HUWYc9mIQIbqxwl9w00mgipdvzFUR9vhREHuDtH7gt7bn4Gfa4i0qxqkdZg5EolHDiD4GPpWYXkg+AUmuvzp+nd9sotKFcKYUe3Lb2FvqE/RRb7Ij3gSWJbQnjbBkqUCBb9G3RUOJLNu8u/T01PJNC3LKZwcceibUEe1pxBNAUbe4s4+NMnRqFn8kSUjK1AcmkI//oFcPYizlZhxxTvkasD09qgcStitZsqFWYaPmUirjH2Rx1KTq2/+bGXI+LmzaBh+Z9lLfYotYP2RWecOsxwuZLpXGBJE0M/YXmtIutqksH8QFinTaGLdIOarE6YblMDbl+jjnkCdXg84BXxs6mWmN4k///rGem1Yb72oczyccVQnCPpGWtGLUmj1RyGqFtXFQaqeI839VmWQx92iI3rlQr1eGTyyoN67i0EWZeMKk80FCxdHiFrF0Khc+aWSol3hEJ+leSB/FpvLUhkN4RiY1XW7rq2EEABMYtzJALvKhStshz9T6WF+NXQoI9deg9rpMdzqmsdBya9RNlVK0YVHrwO7Dk3s6LX8zoNayU="
            )
        )
    }

    private fun createSpanishItem(): ItemStack {
        return buildLangItem(
            "Español",
            "¿Hablas español?",
            "es",
            PlayerSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzAxMjgwMjUzMSwKICAicHJvZmlsZUlkIiA6ICIzOTdlMmY5OTAyNmI0NjI1OTcyNTM1OTNjODgyZjRmMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ4WnlkdWVMeCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iZmFmNTRlNmQ5NTljM2MwMzRhNDIwOTI3YTc4NzY5NDhjYzg3MzdjYTNiYThjNWJiODM2YzUyOTAxZjJiODE2IgogICAgfQogIH0KfQ==",
                "ksp9Bifcz3EWQ/+xk0C6AqQwcqFzUyibgsnsWJi7FQd9Feh6xOHIxsgHSP74mlhgg1Pp7OBmTm5YQhCdR++tr7HugIhhiuNLZr17ZfRWKaKA2utgLzF5RdODZquwYf8SYWjVvzX8LsKY9UeW8lTdZ4unNYcX1oxz14DJAbxLYyWaGBUGkVX1QjhFti+Y1hfZNAKGHiZsPoazw57Y/Da344SOgkFfUHAwgNfzmuaRzimn5g2sdvxXvrXZ0UgEaoTeBg4vy4DkA+w2vvfm0G2WEeV9HncZGAZbjNSqeqUrKyl1F06//OYIRH/JJjWJxuudj/run5T+1PvXUFee/72TnbbKS8AjKGeXPtGqoFLvsOZPyrAPXEP0Wr81l73rxlXeBclTioB1q8GevkuPhXpJtC1qwUH9QBco+kNOPhPF8QsrndtyxpvWj+g9y8RTvFWpwn9G62cAxj9oHO3hAzJ9W0p2OPkz6qskSxaZRJ7EQ9IBUdusZAb/LHCnG8e6af/yXBUv80cfshjUXyEHPYBPPiGekOCQKf0ZglZ/c1rw3VamH/y8rM75dAAa/zTKl/iJj+19X0OZ6w1vXIq9UhpFkWUDMi2jIwgJS7XmeEv1TeCkoMXei1pmlJipF1jSI8sNMqy07F5xgPe/yddOd7DRmMAvWvYn+xn8VUPvuZkYwTs="
            )
        )
    }

    private fun buildLangItem(language: String, lore: String, langTag:String, skin: PlayerSkin): ItemStack {
        return ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(Component.text(language, NamedTextColor.GOLD).withBold().withoutItalic())
            .meta(PlayerHeadMeta::class.java) {
                it.skullOwner(UUID.randomUUID())
                it.playerSkin(skin)
                it.setTag(Tag.String("lang"), langTag)
            }
            .lore(Component.text(lore, NamedTextColor.GRAY))
            .build()
    }
}