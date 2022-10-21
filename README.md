# intellij-project-sanity

This is a small tool that modifies an IntelliJ IDEA project for Gradle (`.idea/gradle.xml`) in accordance to my liking.

The problem is that a Gradle-synced Intellij IDEA project have two modes of running: using IntelliJ, or using Gradle.
The default mode of running (both apps and tests) is with Gradle. I, however, find that the run-with-IntelliJ mode works
better in many cases. For example, the run-with-Gradle mode does not properly support gracefully shutting down
applications – when pressing the "stop" button, it will just kill the process.

I also tend to frequently create or recreate IDEA projects. The result is that I often need to go into these settings
("Preferences" -> "Gradle") and change both "Build and run using:" and "Run tests using:". That is annoying. This tool
does the work for me.

## Notes

Any run configurations that you create while in run-with-Gradle mode will stay as such. You'll have to remove them and
recreate them.
