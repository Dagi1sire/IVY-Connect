# IVY Parent Connect - Android Application

Communication and announcement platform for IVY Childcare Services with event calendars, RSVPs, bilingual English & Amharic announcements, offline-first Room database, and Firebase synchronization.

---

## Automated APK Builds with GitHub Actions

This repository includes a pre-configured GitHub Actions CI/CD workflow located at `.github/workflows/build-apk.yml`. Every time changes are pushed or merged into `main` or `master`, GitHub Actions automatically compiles the application into an installable Android APK (`.apk`).

### How to Export & Push from Google AI Studio to GitHub

1. In **Google AI Studio**, open the top-right settings/export menu (or click the GitHub icon / Project settings).
2. Select **Export to GitHub** (or **Push to GitHub**).
3. Connect your GitHub account and choose or create a repository (e.g. `ivy-parent-connect`).
4. Complete the export. AI Studio will push all source files, Gradle scripts, configuration files, and the `.github/workflows/build-apk.yml` workflow to your GitHub repo.

---

### How to Download the Compiled APK from GitHub Actions

Once pushed to GitHub:

1. Open your repository on GitHub in your web browser.
2. Click on the **Actions** tab at the top of the repository.
3. Under **All workflows**, click on the latest workflow run (named **"Build & Package Android APK"**).
4. Scroll down to the **Artifacts** section at the bottom of the summary page.
5. Click on **`ivy-parent-connect-debug-apk`** to download the ZIP file containing your compiled APK (`ivy-parent-connect-debug.apk`).
6. Unzip the file and install `ivy-parent-connect-debug.apk` directly onto your Android device or emulator.

---

### Manual Trigger & Releases

- **Manual Trigger**: Under the **Actions** tab, select **Build & Package Android APK**, click **Run workflow**, and optionally toggle "Create a GitHub Release with the APK".
- **Release Tags**: Pushing any tag starting with `v` (e.g., `git tag v1.0.0 && git push origin v1.0.0`) will automatically build the APK and create a GitHub Release with the APK attached under the **Releases** tab.

---

### Local Build Instructions

If building locally via terminal:

```bash
chmod +x gradlew
./gradlew assembleDebug
```
The output APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`
