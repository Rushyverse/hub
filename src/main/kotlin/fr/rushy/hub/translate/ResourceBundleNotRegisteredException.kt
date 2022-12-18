package fr.rushy.hub.translate

import java.util.*

/**
 * Exception used when the system try to use a resource bundle not registered.
 */
class ResourceBundleNotRegisteredException(val bundleName: String, val locale: Locale) : RuntimeException("The bundle [$bundleName] for locale [$locale] is not registered.")