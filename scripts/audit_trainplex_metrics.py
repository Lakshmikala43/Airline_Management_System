import os

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

PROD_DIRS = [
    os.path.join(PROJECT_ROOT, 'backend', 'src', 'main', 'java'),
    os.path.join(PROJECT_ROOT, 'frontend', 'src'),
    os.path.join(PROJECT_ROOT, 'scripts')
]

VALID_EXTS = {'.java', '.ts', '.tsx', '.py', '.sql', '.css'}

def is_prod_line(line, ext):
    stripped = line.strip()
    if not stripped:
        return False
    if ext in ['.java', '.ts', '.tsx', '.css']:
        if stripped.startswith('//') or stripped.startswith('/*') or stripped.startswith('*') or stripped.endswith('*/'):
            return False
    elif ext == '.py':
        if stripped.startswith('#'):
            return False
    elif ext == '.sql':
        if stripped.startswith('--') or stripped.startswith('/*'):
            return False
    return True

def audit():
    total_files = 0
    total_prod_loc = 0
    lang_loc = {}

    for d in PROD_DIRS:
        for root, dirs, files in os.walk(d):
            if 'test' in root or 'tests' in root:
                continue
            for f in files:
                ext = os.path.splitext(f)[1].lower()
                if ext in VALID_EXTS:
                    filepath = os.path.join(root, f)
                    loc = 0
                    try:
                        with open(filepath, 'r', encoding='utf-8', errors='ignore') as file:
                            for line in file:
                                if is_prod_line(line, ext):
                                    loc += 1
                    except Exception:
                        pass
                    total_files += 1
                    total_prod_loc += loc
                    lang_loc[ext] = lang_loc.get(ext, 0) + loc

    print("=" * 65)
    print(" TRAINPLEX PROD LOC AUDIT RESULT ")
    print("=" * 65)
    print(f"Total Production LOC : {total_prod_loc:,d}")
    print(f"Total Prod Files     : {total_files:,d}")
    print(f"Required Target      : 50,000 LOC")
    print(f"Audit Result         : {'PASS [OK]' if total_prod_loc >= 50000 else 'FAIL'}")
    print("-" * 65)
    for ext, loc in lang_loc.items():
        print(f"  Language {ext:6s}: {loc:,d} LOC")
    print("=" * 65)

if __name__ == '__main__':
    audit()
