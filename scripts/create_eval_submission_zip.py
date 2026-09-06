import os
import zipfile
import shutil

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
OUTPUT_ZIP_PATH = os.path.join(PROJECT_ROOT, 'airline-management-system-eval.zip')
DESKTOP_ZIP_PATH = r'C:\Users\WINDOWS\Desktop\airline-management-system-eval.zip'

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated', '__pycache__'
}

EXCLUDE_EXTS = {
    '.zip', '.tar', '.gz', '.log', '.jar', '.war', '.class', '.pyc'
}

def create_eval_zip():
    print("Creating official TrainPlex submission zip WITH .git repository history...")
    
    file_count = 0

    with zipfile.ZipFile(OUTPUT_ZIP_PATH, 'w', zipfile.ZIP_DEFLATED, compresslevel=9) as zipf:
        for root, dirs, files in os.walk(PROJECT_ROOT):
            dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]

            for file in files:
                ext = os.path.splitext(file)[1].lower()
                if ext in EXCLUDE_EXTS:
                    continue

                filepath = os.path.join(root, file)
                if os.path.abspath(filepath) == os.path.abspath(OUTPUT_ZIP_PATH):
                    continue

                arcname = os.path.relpath(filepath, PROJECT_ROOT)
                zipf.write(filepath, arcname)
                file_count += 1

    shutil.copy(OUTPUT_ZIP_PATH, DESKTOP_ZIP_PATH)

    size_mb = os.path.getsize(OUTPUT_ZIP_PATH) / (1024 * 1024)
    print("\n" + "=" * 65)
    print("[OK] Official TrainPlex submission zip created successfully!")
    print(f"     - Zip Location : {OUTPUT_ZIP_PATH}")
    print(f"     - Desktop Copy : {DESKTOP_ZIP_PATH}")
    print(f"     - Includes .git: YES (Contains commit history & PR merges)")
    print(f"     - Total Files  : {file_count:,d}")
    print(f"     - File Size    : {size_mb:.2f} MB")
    print("=" * 65)

if __name__ == '__main__':
    create_eval_zip()
