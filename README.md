# Nbt Model Mod

## Setup

clone this repository to your pc and `gradle publishToMavenLocal` to your local maven.

and add these lines to your `build.gradle`

```groovy
repositories {
    mavenLocal()
}

dependencies {
	modImplementation ""
}
```

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
