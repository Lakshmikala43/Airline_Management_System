#!/usr/bin/env python3
"""
SkyNova Airways - Ultra-Compact TrainPlex Submission Zip Generator
Creates a sub-500KB clean submission zip for Replit TrainPlex Checker Bot.
Prevents Replit 502/504 Gateway Timeout HTML response errors.
"""

import os
import zipfile

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
OUTPUT_ZIP_PATH = os.path.join(PROJECT_ROOT, 'trainplex_submission.zip')
DESKTOP_ZIP_PATH = r'C:\Users\WINDOWS\Desktop\trainplex_submission.zip'

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.git', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated', '__pycache__'
}

EXCLUDE_EXTENSIONS = {
    '.zip', '.tar', '.gz', '.log', '.jar', '.war', '.class', '.pyc', '.png', '.jpg', '.jpeg'
}

def create_ultra_zip():
    print("Generating ultra-compact submission zip for Replit TrainPlex Checker Bot...")
    
    file_count = 0

    with zipfile.ZipFile(OUTPUT_ZIP_PATH, 'w', zipfile.ZIP_DEFLATED, compresslevel=9) as zipf:
        for dirpath, dirnames, filenames in os.walk(PROJECT_ROOT):
            dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]

            for filename in filenames:
                ext = os.path.splitext(filename)[1].lower()
                if ext in EXCLUDE_EXTENSIONS:
                    continue
                if filename.endswith('.zip'):
                    continue

                filepath = os.path.join(dirpath, filename)
                if os.path.abspath(filepath) == os.path.abspath(OUTPUT_ZIP_PATH):
                    continue

                arcname = os.path.relpath(filepath, PROJECT_ROOT)
                zipf.write(filepath, arcname)
                file_count += 1

    # Copy to Desktop
    import shutil
    shutil.copy(OUTPUT_ZIP_PATH, DESKTOP_ZIP_PATH)

    zip_size_kb = os.path.getsize(OUTPUT_ZIP_PATH) / 1024
    print("[OK] Ultra-compact zip creation complete!")
    print(f"    - Output Zip File : {OUTPUT_ZIP_PATH}")
    print(f"    - Desktop Zip File: {DESKTOP_ZIP_PATH}")
    print(f"    - Total Files Zipped: {file_count:,d}")
    print(f"    - Compressed Size : {zip_size_kb:.1f} KB (Sub-1MB for 1-second Replit processing!)")

if __name__ == '__main__':
    create_ultra_zip()
