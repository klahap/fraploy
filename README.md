# Fraploy - Frappe App Deployer

Fraploy is a Gradle plugin designed to streamline the deployment of Frappe app updates to Frappe Cloud. With easy
configuration and seamless integration into your build pipeline, you can automate the deployment process, reducing
manual steps and potential errors.

## Features

- Deploy updates to Frappe apps on Frappe Cloud.
- Configurable via Gradle Kotlin DSL.
- Supports credentials management through environment variables.
- Blocking mode with configurable polling delay.

## Installation

Add the `fraploy` plugin to your `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.klahap.fraploy") version "$VERSION"
}
```

## Usage

To configure the plugin, add the following configuration block to your `build.gradle.kts`:

```kotlin
fraploy {
    credentials {
        token = System.getenv("FRAPPE_CLOUD_TOKEN")
        team = System.getenv("FRAPPE_CLOUD_TEAM")
    }
    source {
        releaseGroupTitle = System.getenv("FRAPPE_CLOUD_RELEASE_GROUP_TITLE")
        addAppUpdate(appName = "frappe", version = "v15.37.0")
        addAppUpdate(appName = "erpnext", version = "v15.32.1")
    }
    blocking {
        enable = true
        pollDelay = 5.seconds
    }
}
```

- **credentials**
    - `token`: Your Frappe Cloud API token.
    - `team`: Your Frappe Cloud team name.

- **source**
    - `releaseGroupTitle`: The title for the release group in Frappe Cloud.
    - `addAppUpdate(appName, version)`: Add an app update to the release. Specify the `appName` and the `version`.

- **blocking**
    - `enable`: Enables or disables blocking mode (default is `false).
    - `pollDelay`: The delay between polls to check the deployment status. Default is 5 seconds.

### Running the Deployment

After configuring the plugin, you can deploy your Frappe app updates with the following command:

```bash
gradle fraployDeploy
```

This command will trigger the deployment process, and the plugin will handle the rest.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.
