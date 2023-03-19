package com.github.rushyverse.ext

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * Transforms a string into a component using MiniMessage.
 * Will set the color according to the tag in the string.
 * The [tagResolver] will be used to resolve the custom tags and replace values.
 * @receiver The string used to create the component.
 * @param tagResolver The tag resolver used to resolve the custom tags.
 * @return The component created from the string.
 */
fun String.asMiniComponent(vararg tagResolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, *tagResolver)