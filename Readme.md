# Android and Spring Messages Wording Gradle Plugin

## Summary

This plugin allow you to manage android app's wording with simple Google Sheet file.
Just create a sheet with columns for keys and wording.
The plugin will generate or update existing strings files.
Your product owner will be able to edit himself wording for Spring or Android applications.

## Quick Start

You can find a sample sheet [here](https://docs.google.com/spreadsheets/d/1UznpBPuRddr5gYPnRhNxkP-bcU0NHkhpTqi6B5V0QL0/edit?usp=sharing) but it's just a simple sheet with one column for keys and columns for languages like this

| Keys           | English   | French |
|----------------|-----------|--------|
| user_firstname | Firstname | Prénom |
| user_lastname  | Lastname  | Nom    |

Add following buildScript in the main root `build.gradle`.

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.com.betomorrow.gradle:gradle-wording-plugin:1.4.0"
    }
}
```

Apply plugin in `app/build.gradle`

```groovy
plugins {
    id "com.betomorrow.gradle.wording"
}

wording {
    sheetId = "1CLtBMPTXJC2SAzdXMK1uIRc7tkVRUTSnGC28RBmzgeU"
    languages {
        'default' {
            column = "C"
        }
        'fr' {
            column = "D"
        }
    }
}
```

Then run `./gradlew :app:upgradeWording`

It will ask you to grant access on Google Sheet

```bash
> Task :app:downloadWording
Please open the following address in your browser:
  https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=971125274965-0glt9eqo63417es0nbhkmb6rj2i31g2p.apps.googleusercontent.com&redirect_uri=http://localhost:8888/Callback&response_type=code&scope=https://www.googleapis.com/auth/drive

```

Copy / Paste url in your browser, accept authorization and close browser

[Authorization Sample](https://github.com/oliviergauthier/gradle-wording-plugin/blob/master/images/authorization.png)

It will update wording files. In this sample, for xml output it will be `app/src/main/res/values/strings.xml` and `app/src/main/res/values-fr/strings.xml`
or for properties output, it will be `src/main/resources/messages.properties` and `src/main/resources/messages_fr.properties`

## Tasks

Plugin creates several tasks to manage wording :

- **downloadWording** : Export sheet in local xlsx file that you can commit for later edit. It prevents risks to have unwanted wording changes when you fix bugs
- **updateWording** : Update all wording files
- **upgradeWording** : Download and update all wording files

It also creates tasks for each defined languages : **updateWordingDefault**, **updateWordingFr**, ...

## Complete DSL

```groovy
wording {
    credentials = "credentials.json"    // Optional, default : use provided credentials  
    clientId = ""                       // Optional, default : use provided credentials  
    clientSecret = ""                   // Optional, default : use provided credentials  

    sheetId = "THE SHEET ID"            // *Required*
    sheetNames = ["commons", "app"]     // Optional, default: use all sheetName
    filename = "wording.xlsx"           // Optional, default: "wording.xlsx"
    keysColumn = "A"                    // Optional, default: "A"

    skipHeaders = true                  // Ski headersOptional, default: true
    addMissingKeys = true               // Add missing key from sheet in wording files, if false, it will throw errors on default wording file when missing keys. Default true

    languages {
        'default' {
            output "src/main/resources/messages.properties" // Optional, default: "src/main/resources/messages.properties"
            column = "B"
        }
        'fr' {
            output = "src/main/resources/messages_france.properties" // Optional, default: "src/main/resources/messages_<LANGUAGE>.properties"
            column = "C"
        }
        // [...] Add more languages here
    }
}

```

## Note

The plugin includes Google Projet credentials for convenience use but you can setup your own projet. Create new project in [GCP Console](https://console.cloud.google.com) then enable **Drive API** in *API library* and create credentials. You can use `credentials.json` file or `clientId` / `clientSecret`

## Known Issue

```bash
Execution failed for task ':app:updateWordingCa'.
> InputStream of class class org.apache.commons.compress.archivers.zip.ZipFile$1 is not implementing InputStreamStatistics.
```

You missed to declare the plugin in the root buildScript classpath

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.com.betomorrow.gradle:gradle-spring-wording-plugin:1.0.0"
    }
}
```