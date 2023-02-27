package com.betomorrow.gradle.wording.domain

interface WordingCleaner {
    fun clean(wording: Map<String, String>): Map<String, String>
}

class WordingCleanerFactory {
    fun build(strategy: Strategy): WordingCleaner {
        return when (strategy) {
            Strategy.SPRING -> SpringWordingCleaner()
            Strategy.NONE -> DumbWordingCleaner()
        }
    }
}

class SpringWordingCleaner : WordingCleaner {

    override fun clean(wording: Map<String, String>): Map<String, String> {
        return wording.mapValues { (_, value) ->
            // if string contains fields to fill like {0}
            if (value.contains("\\{\\d+\\}".toRegex())) {
                // then quotes must be doubled
                value.replace("'", "''")
            } else {
                value
            }
        }
    }
}

class DumbWordingCleaner : WordingCleaner {

    override fun clean(wording: Map<String, String>): Map<String, String> {
        return wording
    }
}

enum class Strategy {
    SPRING,
    NONE,
}
