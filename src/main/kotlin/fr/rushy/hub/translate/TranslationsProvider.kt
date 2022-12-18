package fr.rushy.hub.translate

import java.util.*

/**
 * Translation provider interface, in charge of taking string keys and returning translated strings.
 */
abstract class TranslationsProvider {

    /**
     * Get a translation by key from the given locale and bundle name.
     */
    abstract fun get(key: String, locale: Locale, bundleName: String): String

    /**
     * Get a formatted translation using the provided arguments.
     */
    abstract fun translate(
        key: String,
        locale: Locale,
        bundleName: String,
        replacements: Array<String>
    ): String

    /**
     * Get a formatted translation using the provided arguments.
     */
    fun translate(
        key: String,
        locale: Locale,
        bundleName: String
    ): String = translate(key, locale, bundleName, emptyArray())

    /**
     * Get a formatted translation using the provided arguments.
     */
    fun translate(
        key: String,
        locale: Locale,
        bundleName: String,
        replacements: Collection<String>
    ): String = translate(key, locale, bundleName, replacements.toTypedArray())
}