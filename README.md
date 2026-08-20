# Usage
Download this mod repository and let gradle do its magic (if you're running it in an IDE)

Most important commands:
 - `gradlew runClient`: starts modded minecraft client
 - `gradlew runData`: runs datagen
 - `gradlew build`: builds a jar in ./build/libs

For building and publishing to the unlisted [Modrinth mod page](https://modrinth.com/mod/coreboundcore):
 - Login to [Modrinth](https://modrinth.com)
 - Go to Settings -> Personal Access Tokens
 - Create a token with permissions to create versions
 - Open the mod directory and set the environment variable `MODRINTH_TOKEN` token to your Personal Access Token (easily done in Intellij IDEA by right clicking on the gradle command, going to Modify Run Configuration and setting the environment variable there)
 - Run the `gradlew modrinth`/`./gradlew modrinth` command once your environment variable has been set
