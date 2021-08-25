package com.looker.core.utils

class Logger(
    private val tag: String,
    private val isDebug: Boolean = true,
) {
    fun log(msg: String) {
        printLogD(tag, msg)
    }

    companion object Factory {
        fun buildDebug(className: String): Logger {
            return Logger(
                tag = className,
                isDebug = true,
            )
        }

        fun buildRelease(className: String): Logger {
            return Logger(
                tag = className,
                isDebug = false,
            )
        }
    }
}

fun printLogD(tag: String?, message: String) {
    println("$tag: $message")
}