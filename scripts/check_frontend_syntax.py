import os

src_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../frontend/src'))

found = False
for root, _, files in os.walk(src_dir):
    for f in files:
        if f.endswith('.ts') or f.endswith('.tsx'):
            path = os.path.join(root, f)
            with open(path, 'r', encoding='utf-8', errors='ignore') as file:
                lines = file.readlines()
                for idx, line in enumerate(lines, 1):
                    if '.compareTo(' in line:
                        print(f"FOUND compareTo in {path}:{idx} -> {line.strip()}")
                        found = True

if not found:
    print("NO compareTo calls found in frontend source files!")
