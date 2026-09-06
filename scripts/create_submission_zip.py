import os
import zipfile

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
OUTPUT_ZIP = os.path.join(PROJECT_ROOT, 'submission.zip')
DESKTOP_ZIP = r'C:\Users\WINDOWS\Desktop\submission.zip'

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.git', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated', '__pycache__'
}

EXCLUDE_EXTS = {
    '.zip', '.tar', '.gz', '.log', '.jar', '.war', '.class', '.pyc'
}

def make_zip():
    print("Creating clean submission.zip (1.1 MB)...")
    file_count = 0
    with zipfile.ZipFile(OUTPUT_ZIP, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, dirs, files in os.walk(PROJECT_ROOT):
            dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]
            for file in files:
                ext = os.path.splitext(file)[1].lower()
                if ext in EXCLUDE_EXTS:
                    continue
                filepath = os.path.join(root, file)
                if os.path.abspath(filepath) == os.path.abspath(OUTPUT_ZIP):
                    continue
                arcname = os.path.relpath(filepath, PROJECT_ROOT)
                zipf.write(filepath, arcname)
                file_count += 1
    
    import shutil
    shutil.copy(OUTPUT_ZIP, DESKTOP_ZIP)
    size_mb = os.path.getsize(OUTPUT_ZIP) / (1024 * 1024)
    print(f"[OK] Done! Created submission.zip ({size_mb:.2f} MB)")
    print(f"     Path: {OUTPUT_ZIP}")
    print(f"     Desktop: {DESKTOP_ZIP}")

if __name__ == '__main__':
    make_zip()
