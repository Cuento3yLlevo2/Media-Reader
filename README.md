# Media Reader

Compose app that creates a number playlist of media content (Images and videos) from a given zip file provided from an API, it contains the resources and a json with instructions of duration and the position, size & duration of the media in the layout.

## How it works 👣

- If haven't yet. the app will start downloading the zip file, unzipping and saving the data in local storage, asynchronously using a WorkManager for the job. 
- If the data is downloaded usefully it will map the json file to process the given playlist instructions.   
- Having the playlist instructions define it will generate the views for the playlist using Composable views in a loop.

## Features 🎨

- Uses Jetpack Compose for Views.
- Uses WorkManager and CoroutineWorkers for large file downloads.
- Handles unexpected errors safety.
- Uses dagger hilt DI. 
- MVVM & clean architecture