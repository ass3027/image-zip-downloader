# Image Zip Downloader

Android app that automates the image translation workflow using the ichigoreader.com translator API.

## Purpose

Replaces the manual process of zipping image folders, uploading to a translator, polling for completion, downloading results, and extracting them — all from a single Android app.

## Automated Workflow

1. User selects an image folder on device
2. App zips the folder
3. App uploads the zip to the translator API
4. App polls for translation completion
5. App downloads the result zip
6. App extracts the downloaded zip to the output directory
7. App deletes the downloaded zip file

## Translator API

API spec is at `api-spec.md`. The flow uses 4 endpoints on `https://ichigoreader.com`:

| Step | Method | Endpoint | Auth |
|------|--------|----------|------|
| Login | POST | `/auth/login` | None (returns tokens) |
| Upload | POST | `/translate/as-reader-format` | `access_cookie` |
| Status | GET | `/translate/as-reader-format/get?jobId={id}` | `access_cookie` |
| Download | GET | `/translate/as-reader-format/download?jobId={id}` | `access_cookie` |

- Authentication is cookie-based (`access_cookie`, `refresh_token_cookie`)
- Upload is multipart/form-data with fields: `file`, `fingerprint`, `targetLangCode`, `translationModel`
- Status polling: check `status` field — transitions through `building-context` → `translating` → done
- Target language: `ko` (Korean)

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Min SDK**: 24, Target SDK: 36
- **Build**: Gradle 9.2.1 with version catalogs (`gradle/libs.versions.toml`)
- **Package**: `com.example.image_zip_downloader`

## Project Structure

```
app/src/main/java/com/example/image_zip_downloader/
├── MainActivity.kt          # Entry point
├── ui/theme/                 # Compose theme (Color, Type, Theme)
```

## UX Requirements

- Default paths should be preconfigured for both the image folder picker and the unzip output directory
- Completed jobs should be listed in the UI; tapping a job navigates to its output directory

## Build

```bash
./gradlew :app:assembleDebug
```
