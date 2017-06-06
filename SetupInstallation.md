# Setup and Installation

## Setting up the develpment environment

### Requirements:
- JDK
- Android SDK
- IntelliJ IDEA is recommended and used in this guide, although other IDE's with similar capabilities should also work.
 
### Step-by-step (Works for TSS and TSSC):
- On IntelliJ, press "Open Project" and open the build.gradle file on the project root directory.
- Select "Open as Project."
- On the window that pops up, untick "Create separate module per source set" and set up your Gradle JVM. Leave everything else on default settings and press "OK".
- Wait for Gradle and IntelliJ to load and configure the project.
- If your Android SDK is not set up, go to File -> Project Structure, click the "New" button, press "Android SDK" and choose your Android SDK path. You might need to update your SDK, go to your SDK Manager to do that.
- You should now have an Android run configuration.
- To run on Desktop, create the following run configuration:
![Desktop Configuration](desktop.png)
- To run the unit tests, create the following run configuration:
![JUnit Configuration](junit.png)
- Profit.