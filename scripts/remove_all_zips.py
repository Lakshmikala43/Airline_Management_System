import os
import glob

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

def remove_all_zip_files():
    print("Removing all .zip files from project root folder as requested...")
    zip_pattern = os.path.join(PROJECT_ROOT, "*.zip")
    zip_files = glob.glob(zip_pattern)
    
    removed = 0
    for filepath in zip_files:
        try:
            os.remove(filepath)
            print(f"  [OK] Deleted zip file: {os.path.basename(filepath)}")
            removed += 1
        except Exception as e:
            print(f"  [!] Could not delete {os.path.basename(filepath)}: {e}")

    print(f"\n[OK] Complete. Deleted {removed} zip files from project root directory.")

if __name__ == '__main__':
    remove_all_zip_files()
