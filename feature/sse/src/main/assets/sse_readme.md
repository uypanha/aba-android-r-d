## Server Sent Events for Android

## Getting started

Add these implementation to your gradle. Current SSE stable version is 5.0.0-alpha.11.

```kotlin
implementation(platform("com.squareup.okhttp3:okhttp-bom:$version"))

implementation("com.squareup.okhttp3:okhttp")
testImplementation("com.squareup.okhttp3:okhttp-sse")
```