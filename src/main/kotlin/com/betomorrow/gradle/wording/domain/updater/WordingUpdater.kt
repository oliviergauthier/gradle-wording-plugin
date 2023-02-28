package com.betomorrow.gradle.wording.domain.updater

interface WordingUpdater {
    /**
     * @return list of updated keys
     */
    fun update(wording: Map<String, String>, addMissingWording: Boolean): Set<String>
}
