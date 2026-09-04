#!/usr/bin/env python3
"""
SkyNova Airways - Clean Submission Zip Generator for TrainPlex Checker Bot
Packs the project into a lightweight zip file excluding node_modules, target, .git, build caches.
Ensures TrainPlex Checker Bot can upload without 502/504 Replit HTTP timeout errors.
"""

import os
import zipfile

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
OUTPUT_ZIP_PATH = os.path.join(PROJECT_ROOT, 'airline-reservation-management-system.zip')

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.git', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated', '__pycache__'
}

EXCLUDE_EXTENSIONS = {
    '.zip', '.tar', '.gz', '.log', '.jar', '.war', '.class', '.pyc'
}

def create_zip():
    print("Generating clean, lightweight submission zip for TrainPlex Checker Bot...")
    
    file_count = 0
    total_uncompressed_bytes = 0

    with zipfile.ZipFile(OUTPUT_ZIP_PATH, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for dirpath, dirnames, filenames in os.walk(PROJECT_ROOT):
            # Exclude unwanted directories
            dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]

            for filename in filenames:
                ext = os.path.splitext(filename)[1].lower()
                if ext in EXCLUDE_EXTENSIONS:
                    continue
                if filename.endswith('.zip') or filename.endswith('.zip.zip'):
                    continue

                filepath = os.path.join(dirpath, filename)
                
                # Do not zip the output zip itself
                if os.path.abspath(filepath) == os.path.abspath(OUTPUT_ZIP_PATH):
                    continue

                arcname = os.path.relpath(filepath, PROJECT_ROOT)
                zipf.write(filepath, arcname)
                
                file_count += 1
                total_uncompressed_bytes += os.path.getsize(filepath)

    zip_size_mb = os.path.getsize(OUTPUT_ZIP_PATH) / (1024 * 1024)
    print("\n[OK] Zip creation complete!")
    print(f"    - Output Zip File : {OUTPUT_ZIP_PATH}")
    print(f"    - Total Files Zipped: {file_count:,d}")
    print(f"    - Compressed Size : {zip_size_mb:.2f} MB (Optimized for TrainPlex upload!)")

if __name__ == '__main__':
    create_zip()
