# DatShaft
Repository for any and all things Shaftware related

Project setup instructions:

1. Clone this repository to your local drive

2. Open in Android Studio and ensure there are no errors
    If Android Studio complains about missing Google SDK files, go into
    Settings/Android SDK and add the Google SDK to your machine.
    
3. The project requires google-services.json from the Firebas Console. This
    is deliberately excluded from the repository as it will vary by machine.
    To obtain this file, log into Firebase Console (google it) with your mtu
    name/password, and go to ShaftQuack->manage. Run the command
    keytool -exportcert -list -v -alias androiddebugkey -keystore "%USERPROFILE%\.android\debug.keystore"
    from the directory that contains the Java SDK (Usually Program Files/Java/bin). Copy the SHA1 key
    (should look like XX:XX:XX...) to the SHA1 section of the Firebase Console. This will ensure that
    apps built from this machine will be able to connect to the Firebase Server.
    
4. Download the google-services.json file from Firebase Console, and put it in the "app" directory of
    the local repository. The .gitignore should already be configured to not sync this file.
    
5. Build and run.
