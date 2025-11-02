# FastCodeGen - Files to Download

This document lists all the files you need to download and where to place them in your project.

## 📥 Download Location

All improved files are available in `/tmp/` directory:

```bash
/tmp/plugin.xml
/tmp/CodeGenDialog.kt
/tmp/StringUtils.kt
/tmp/PackageUtils.kt
/tmp/FileUtils.kt
/tmp/UIConstants.kt
/tmp/README.md
/tmp/USER_GUIDE.md
/tmp/PLUGIN_SUMMARY.md
/tmp/IMPLEMENTATION_STEPS.md
/tmp/QUICK_REFERENCE.md
```

## 📂 File Placement Guide

### 1. Configuration Files

#### plugin.xml
- **Source:** `/tmp/plugin.xml`
- **Destination:** `src/main/resources/META-INF/plugin.xml`
- **Action:** Replace existing file
- **Purpose:** Plugin metadata with your contact info

### 2. Kotlin Source Files

#### CodeGenDialog.kt
- **Source:** `/tmp/CodeGenDialog.kt`
- **Destination:** `src/main/kotlin/com/alfayedoficial/fastcodegen/dialog/CodeGenDialog.kt`
- **Action:** Replace existing file
- **Purpose:** Improved UI with better styling

#### StringUtils.kt
- **Source:** `/tmp/StringUtils.kt`
- **Destination:** `src/main/kotlin/com/alfayedoficial/fastcodegen/utils/StringUtils.kt`
- **Action:** Create new file (create utils directory if needed)
- **Purpose:** String manipulation utilities

#### PackageUtils.kt
- **Source:** `/tmp/PackageUtils.kt`
- **Destination:** `src/main/kotlin/com/alfayedoficial/fastcodegen/utils/PackageUtils.kt`
- **Action:** Create new file
- **Purpose:** Package name operations

#### FileUtils.kt
- **Source:** `/tmp/FileUtils.kt`
- **Destination:** `src/main/kotlin/com/alfayedoficial/fastcodegen/utils/FileUtils.kt`
- **Action:** Create new file
- **Purpose:** File creation operations

#### UIConstants.kt
- **Source:** `/tmp/UIConstants.kt`
- **Destination:** `src/main/kotlin/com/alfayedoficial/fastcodegen/ui/UIConstants.kt`
- **Action:** Create new file (create ui directory if needed)
- **Purpose:** UI constants for consistent styling

### 3. Documentation Files

#### README.md
- **Source:** `/tmp/README.md`
- **Destination:** `README.md` (project root)
- **Action:** Replace or create
- **Purpose:** Main project documentation

#### USER_GUIDE.md
- **Source:** `/tmp/USER_GUIDE.md`
- **Destination:** `USER_GUIDE.md` (project root)
- **Action:** Create new file
- **Purpose:** Detailed usage guide

#### PLUGIN_SUMMARY.md
- **Source:** `/tmp/PLUGIN_SUMMARY.md`
- **Destination:** `PLUGIN_SUMMARY.md` (project root)
- **Action:** Create new file
- **Purpose:** Complete overview of plugin structure

#### IMPLEMENTATION_STEPS.md
- **Source:** `/tmp/IMPLEMENTATION_STEPS.md`
- **Destination:** `IMPLEMENTATION_STEPS.md` (project root)
- **Action:** Create new file
- **Purpose:** Step-by-step implementation guide

#### QUICK_REFERENCE.md
- **Source:** `/tmp/QUICK_REFERENCE.md`
- **Destination:** `QUICK_REFERENCE.md` (project root)
- **Action:** Create new file
- **Purpose:** Quick reference guide

## 🔄 Files to Modify (Manual Changes)

These files need manual updates to use the new utilities:

### ViewModelStateGenerator.kt
**Location:** `src/main/kotlin/com/alfayedoficial/fastcodegen/generator/ViewModelStateGenerator.kt`

**Changes needed:**
1. Add imports:
```kotlin
import com.alfayedoficial.fastcodegen.utils.StringUtils
import com.alfayedoficial.fastcodegen.utils.PackageUtils
import com.alfayedoficial.fastcodegen.utils.FileUtils
```

2. Remove these methods (now in utilities):
   - `toCamelCase()`
   - `toPascalCase()`
   - `getPackageName()`
   - `createKotlinFile()`
   - `findOrCreateSubdirectory()` (if exists)

3. Update generate() method to use utilities:
```kotlin
val featureFolder = StringUtils.toCamelCase(featureName)
val featureClass = StringUtils.toPascalCase(featureName)

val featureDir = PackageUtils.findOrCreateSubdirectory(baseDirectory, featureFolder)
// ... etc

val basePackage = PackageUtils.getPackageName(project, baseDirectory)

FileUtils.createKotlinFile(project, stateDir, "${featureClass}State.kt", stateContent)
```

### RepoGenerator.kt
**Location:** `src/main/kotlin/com/alfayedoficial/fastcodegen/generator/RepoGenerator.kt`

**Apply same changes as ViewModelStateGenerator.kt**

### FeatureGenerator.kt
**No changes needed** - it just orchestrates the other generators

## 📋 Complete File Tree After Changes

```
FastCodeGen/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/alfayedoficial/fastcodegen/
│   │   │       ├── action/
│   │   │       │   └── FastCodeGenAction.kt
│   │   │       ├── dialog/
│   │   │       │   └── CodeGenDialog.kt ← UPDATED
│   │   │       ├── generator/
│   │   │       │   ├── ViewModelStateGenerator.kt ← MODIFY
│   │   │       │   ├── RepoGenerator.kt ← MODIFY
│   │   │       │   └── FeatureGenerator.kt
│   │   │       ├── ui/ ← NEW DIRECTORY
│   │   │       │   └── UIConstants.kt ← NEW FILE
│   │   │       └── utils/ ← NEW DIRECTORY
│   │   │           ├── StringUtils.kt ← NEW FILE
│   │   │           ├── PackageUtils.kt ← NEW FILE
│   │   │           └── FileUtils.kt ← NEW FILE
│   │   └── resources/
│   │       └── META-INF/
│   │           ├── plugin.xml ← UPDATED
│   │           └── pluginIcon.svg (optional)
│   └── test/
├── build.gradle.kts
├── README.md ← UPDATED
├── USER_GUIDE.md ← NEW FILE
├── PLUGIN_SUMMARY.md ← NEW FILE
├── IMPLEMENTATION_STEPS.md ← NEW FILE
├── QUICK_REFERENCE.md ← NEW FILE
├── LICENSE (MIT)
└── .gitignore
```

## ⚙️ Step-by-Step Implementation

### Option 1: Manual Copy (Recommended)

1. **Create new directories:**
   ```bash
   mkdir -p src/main/kotlin/com/alfayedoficial/fastcodegen/utils
   mkdir -p src/main/kotlin/com/alfayedoficial/fastcodegen/ui
   ```

2. **Copy utility files:**
   - Copy `StringUtils.kt` to `utils/`
   - Copy `PackageUtils.kt` to `utils/`
   - Copy `FileUtils.kt` to `utils/`
   - Copy `UIConstants.kt` to `ui/`

3. **Replace configuration:**
   - Replace `plugin.xml`

4. **Replace dialog:**
   - Replace `CodeGenDialog.kt`

5. **Update generators:**
   - Modify `ViewModelStateGenerator.kt` (see changes above)
   - Modify `RepoGenerator.kt` (see changes above)

6. **Add documentation:**
   - Copy all `.md` files to project root

7. **Sync and build:**
   ```bash
   ./gradlew clean build
   ```

### Option 2: Script (Advanced)

Create a script `update_plugin.sh`:

```bash
#!/bin/bash

# Colors
GREEN='\033[0;32m'
NC='\033[0m'

echo "Updating FastCodeGen plugin..."

# Create directories
mkdir -p src/main/kotlin/com/alfayedoficial/fastcodegen/utils
mkdir -p src/main/kotlin/com/alfayedoficial/fastcodegen/ui

# Copy files
cp /tmp/StringUtils.kt src/main/kotlin/com/alfayedoficial/fastcodegen/utils/
cp /tmp/PackageUtils.kt src/main/kotlin/com/alfayedoficial/fastcodegen/utils/
cp /tmp/FileUtils.kt src/main/kotlin/com/alfayedoficial/fastcodegen/utils/
cp /tmp/UIConstants.kt src/main/kotlin/com/alfayedoficial/fastcodegen/ui/
cp /tmp/CodeGenDialog.kt src/main/kotlin/com/alfayedoficial/fastcodegen/dialog/
cp /tmp/plugin.xml src/main/resources/META-INF/

# Copy documentation
cp /tmp/README.md .
cp /tmp/USER_GUIDE.md .
cp /tmp/PLUGIN_SUMMARY.md .
cp /tmp/IMPLEMENTATION_STEPS.md .
cp /tmp/QUICK_REFERENCE.md .

echo -e "${GREEN}✓ Files copied successfully!${NC}"
echo ""
echo "Next steps:"
echo "1. Manually update ViewModelStateGenerator.kt"
echo "2. Manually update RepoGenerator.kt"
echo "3. Run: ./gradlew clean build"
echo "4. Test: ./gradlew runIde"
```

Then run:
```bash
chmod +x update_plugin.sh
./update_plugin.sh
```

## ✅ Verification Checklist

After copying all files:

- [ ] All new files are in correct locations
- [ ] No compilation errors
- [ ] ViewModelStateGenerator uses utilities
- [ ] RepoGenerator uses utilities
- [ ] Plugin builds successfully
- [ ] Plugin runs in test IDE
- [ ] All three generation modes work
- [ ] UI looks good
- [ ] Tooltips appear
- [ ] Documentation is complete

## 🐛 Troubleshooting

### Import Errors
**Problem:** Cannot resolve StringUtils, PackageUtils, etc.

**Solution:**
1. Check files are in correct directories
2. Verify package declarations match folder structure
3. Rebuild project: `./gradlew clean build`
4. Invalidate IDE caches: File → Invalidate Caches → Invalidate and Restart

### Compilation Errors
**Problem:** Code doesn't compile after changes

**Solution:**
1. Check you removed old methods from generators
2. Verify all utility imports are added
3. Check method signatures match
4. Review the IMPLEMENTATION_STEPS.md file

### Files Not Found
**Problem:** Some files missing

**Solution:**
1. Download all files from `/tmp/`
2. Check file names exactly match (case-sensitive)
3. Verify directory structure is correct

## 📞 Need Help?

If you encounter issues:
1. Check IMPLEMENTATION_STEPS.md for detailed instructions
2. Review PLUGIN_SUMMARY.md for overview
3. Contact via email: alialfayed.official@gmail.com
4. Connect on LinkedIn: https://www.linkedin.com/in/alfayedoficial/

## 🎉 Success!

Once all files are in place and modified:

```bash
./gradlew clean build
./gradlew runIde
```

Test all three generation modes and verify everything works!

---

**Next:** Follow IMPLEMENTATION_STEPS.md for detailed modification instructions.
