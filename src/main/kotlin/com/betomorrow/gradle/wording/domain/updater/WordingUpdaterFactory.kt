package com.betomorrow.gradle.wording.domain.updater

import com.betomorrow.gradle.wording.domain.OutputFormat
import java.nio.file.Path

class WordingUpdaterFactory {
    fun build(outputFormat: OutputFormat, path: Path): WordingUpdater {
        return when (outputFormat) {
            OutputFormat.XML -> XmlWordingUpdater(path)
            OutputFormat.PROPERTIES -> PropertiesWordingUpdater(path)
        }
    }
}
