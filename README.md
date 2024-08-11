# Gemini Enhanced API Library

## Overview
This library extends the Gemini Android API by adding a unique feature that allows users to upload a file and a prompt when calling the Gemini getContent API. This extra provision enables the Gemini API to analyze the uploaded file and provide a response based on the content of that file, in addition to the prompt provided.

## Features
- Extended Gemini API: Includes all the functionality of the official Gemini getContent API.
- File Upload Provision: Allows users to upload a file as part of their request to the getContent API.
- Prompt-Driven Analysis: Users can provide a prompt and the file to guide the content analysis.

## Installation
To use this library in your Android project, add the following Maven repository and dependency to your build.gradle file:

```
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/shreyas-android/GeminiEnhancedAPI")
        credentials {
            username =  "YOUR_GITHUB_USER_NAME"
            password = "YOUR_GITHUB_ACCESS_TOKEN" // Get personal token with read package provision
        }
    }
}

dependencies {
    implementation("com.androidai.shared.gemini.enhanced.model:model-android:1.0.0-alpha01")
}

```

## Usage
### Initialize the Library
Before using the extended API, initialize the library in your application:

```
GeminiAIAndroidCore.init(isDebug, apiKey, modelName);
```
- **isDebug:** Set to true for debug mode and false for production.
- **apiKey:** Your Gemini API key.
- **modelName:** The name of the model you wish to use.


### Calling the generateTextStreamContent API

The generateTextStreamContent method is a suspend function that generates content by processing a list of ModelInput objects. The method uses the inputs to interact with Gemini, allowing for the upload of files or other data types, and streams the content generation process.

```
  GeminiAIAndroidCore.getGeminiAIManagerInstance().generateTextStreamContent(modelInputList : List<ModelInput>,
                                          onFileUploadListener : OnFileUploadListener?,
                                          defaultErrorMessage : String): CommonFlow<GeminiAIGenerate>
```


