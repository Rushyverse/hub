package com.github.rushyverse.hub.extension

import org.bukkit.event.Cancellable

fun Cancellable.cancel() {
    isCancelled = true
}

fun <T : Cancellable> T.cancelIf(condition: T.() -> Boolean) {
    if (condition()) {
        cancel()
    }
}