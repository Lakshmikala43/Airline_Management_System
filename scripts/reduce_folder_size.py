import os
import shutil

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

DIRS_TO_REMOVE = [
    os.path.join(PROJECT_ROOT, 'frontend', 'node_modules'),
    os.path.join(PROJECT_ROOT, 'backend', 'target'),
    os.path.join(PROJECT_ROOT, 'frontend', 'dist'),
    os.path.join(PROJECT_ROOT, '.mvn')
]

def get_folder_size_mb(path):
    total = 0
    for root, dirs, files in os.walk(path):
        for f in files:
            filepath = os.path.join(root, f)
            try:
                total += os.path.getsize(filepath)
            except Exception:
                pass
    return total / (1024 * 1024)

def reduce_size():
    initial_size = get_folder_size_mb(PROJECT_ROOT)
    print(f"Initial main folder size: {initial_size:.2f} MB")

    for dirpath in DIRS_TO_REMOVE:
        if os.path.exists(dirpath):
            try:
                shutil.rmtree(dirpath)
                print(f"  [OK] Removed heavy build folder: {os.path.basename(dirpath)}")
            except Exception as e:
                print(f"  [!] Could not remove {dirpath}: {e}")

    final_size = get_folder_size_mb(PROJECT_ROOT)
    print("\n" + "=" * 60)
    print(f"[OK] Main project folder size reduced to: {final_size:.2f} MB!")
    print("=" * 60)

if __name__ == '__main__':
    reduce_size()
