package com.betomorrow.gradle.wording.domain

class WordingCleaner {

    fun clean(wording: Map<String, String>): Map<String, String> {
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
