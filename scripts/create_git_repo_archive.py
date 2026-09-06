#!/usr/bin/env python3
"""
SkyNova Airways - Git Repo Archive Generator for TrainPlex Checker Bot
Packs the git repository including .git metadata but excluding node_modules/target binaries.
Ensures TrainPlex git repo collectors can evaluate the repo archive without size limits.
"""

import os
import zipfile

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
OUTPUT_ZIP_PATH = os.path.join(PROJECT_ROOT, 'airline-management-system-repo.zip')

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated', '__pycache__'
}

EXCLUDE_EXTENSIONS = {
    '.zip', '.tar', '.gz', '.log', '.jar', '.war', '.class', '.pyc'
}

def create_git_zip():
    print("Generating git repo submission zip for TrainPlex Checker Bot...")
    
    file_count = 0

    with zipfile.ZipFile(OUTPUT_ZIP_PATH, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for dirpath, dirnames, filenames in os.walk(PROJECT_ROOT):
            # Exclude unwanted directories except .git
            dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]

            for filename in filenames:
                ext = os.path.splitext(filename)[1].lower()
                if ext in EXCLUDE_EXTENSIONS:
                    continue
                if filename.endswith('.zip') or filename.endswith('.zip.zip'):
                    continue

                filepath = os.path.join(dirpath, filename)
                
                if os.path.abspath(filepath) == os.path.abspath(OUTPUT_ZIP_PATH):
                    continue

                arcname = os.path.relpath(filepath, PROJECT_ROOT)
                zipf.write(filepath, arcname)
                file_count += 1

    zip_size_mb = os.path.getsize(OUTPUT_ZIP_PATH) / (1024 * 1024)
    print("[OK] Git repo zip creation complete!")
    print(f"    - Output Zip File : {OUTPUT_ZIP_PATH}")
    print(f"    - Total Files Zipped: {file_count:,d}")
    print(f"    - Compressed Size : {zip_size_mb:.2f} MB")

if __name__ == '__main__':
    create_git_zip()
