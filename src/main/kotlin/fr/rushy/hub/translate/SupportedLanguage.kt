package fr.rushy.hub.translate

import java.util.*


/**
 * List of supported locales to translate keys.
 */
enum class SupportedLanguage(val displayName: String, val locale: Locale) {

    ENGLISH("English", Locale("en", "gb")),
    FRENCH("Français", Locale("fr", "fr"))

}