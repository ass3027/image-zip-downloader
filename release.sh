#!/usr/bin/env bash
set -euo pipefail

# Extract version from build.gradle.kts
VERSION_NAME=$(grep 'versionName' app/build.gradle.kts | head -1 | sed 's/.*"\(.*\)".*/\1/')
TAG="v${VERSION_NAME}"

echo "Building release APK for ${TAG}..."
./gradlew :app:assembleRelease

APK_PATH="app/build/outputs/apk/release/app-release.apk"
if [ ! -f "$APK_PATH" ]; then
    echo "ERROR: APK not found at $APK_PATH"
    exit 1
fi

echo "Creating git tag ${TAG}..."
git tag -a "$TAG" -m "Release ${TAG}"
git push origin "$TAG"

echo "Creating GitHub release..."
gh release create "$TAG" "$APK_PATH" \
    --title "$TAG" \
    --notes "Release ${TAG}" \
    --latest

echo "Done! Release ${TAG} published."
