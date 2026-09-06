import os

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

CLEANUP_ZIP_FILES = [
    'airline-management-system-repo.zip',
    'trainplex_fast.zip',
    'trainplex_submission.zip'
]

def clean_root_directory():
    print("Cleaning redundant zip files from main project folder...")
    cleaned = 0
    for filename in CLEANUP_ZIP_FILES:
        filepath = os.path.join(PROJECT_ROOT, filename)
        if os.path.exists(filepath):
            try:
                os.remove(filepath)
                print(f"  [OK] Removed redundant file: {filename}")
                cleaned += 1
            except Exception as e:
                print(f"  [!] Could not remove {filename}: {e}")

    print(f"\n[OK] Cleanup complete. Removed {cleaned} redundant zip files.")

if __name__ == '__main__':
    clean_root_directory()
