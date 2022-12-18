package fr.rushy.hub.utils

import java.io.File

/**
 * Get the current directory where is executed the program.
 */
val workingDirectory: File
    get() = File(System.getProperty("user.dir"))