#!/usr/bin/env python3
"""
SkyNova Airways - Line of Code (LOC) Counter
Measures meaningful lines of source code across Java, TypeScript, SQL, Python, CSS, and Configs.
Excludes empty lines, comments, target, node_modules, dist, build, and binary files.
"""

import os
import sys

EXCLUDE_DIRS = {
    'node_modules', 'target', 'dist', 'build', '.git', '.idea', '.vscode',
    '.mvn', 'tools', 'scratch', '.system_generated'
}

VALID_EXTENSIONS = {
    '.java': 'Backend Java Source',
    '.ts': 'TypeScript Source',
    '.tsx': 'React TSX Component',
    '.sql': 'Database Migrations & Seed',
    '.py': 'Python Scripts & Tools',
    '.css': 'Stylesheets',
    '.md': 'Documentation & Reports',
    '.xml': 'Maven Configuration',
    '.yml': 'Application Configuration',
    '.yaml': 'Docker & CI Configuration'
}

def is_meaningful_line(line, ext):
    stripped = line.strip()
    if not stripped:
        return False
    
    # Language-specific comment checks
    if ext in ['.java', '.ts', '.tsx', '.css']:
        if stripped.startswith('//') or stripped.startswith('/*') or stripped.startswith('*') or stripped.endswith('*/'):
            return False
    elif ext in ['.py', '.yml', '.yaml']:
        if stripped.startswith('#'):
            return False
    elif ext == '.sql':
        if stripped.startswith('--') or stripped.startswith('/*'):
            return False
            
    return True

def count_file_loc(filepath, ext):
    count = 0
    try:
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            for line in f:
                if is_meaningful_line(line, ext):
                    count += 1
    except Exception as e:
        print(f"Warning: Could not read {filepath}: {e}")
    return count

def scan_project(root_dir):
    loc_by_category = {
        'Frontend (React/TS/CSS)': 0,
        'Backend (Java/Spring)': 0,
        'Database (SQL)': 0,
        'Automated Tests': 0,
        'Scripts & Tools (Python)': 0,
        'Documentation & Configs': 0
    }
    
    file_counts = {ext: 0 for ext in VALID_EXTENSIONS}
    file_locs = {ext: 0 for ext in VALID_EXTENSIONS}
    total_files = 0
    total_loc = 0

    for dirpath, dirnames, filenames in os.walk(root_dir):
        # Skip excluded directories
        dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]
        
        for filename in filenames:
            ext = os.path.splitext(filename)[1].lower()
            if ext in VALID_EXTENSIONS:
                filepath = os.path.join(dirpath, filename)
                loc = count_file_loc(filepath, ext)
                
                total_files += 1
                total_loc += loc
                file_counts[ext] += 1
                file_locs[ext] += loc
                
                # Categorize
                rel_path = os.path.relpath(filepath, root_dir).replace('\\', '/')
                
                if 'src/test' in rel_path or 'frontend/tests' in rel_path or 'tests/' in rel_path:
                    loc_by_category['Automated Tests'] += loc
                elif 'frontend/src' in rel_path:
                    loc_by_category['Frontend (React/TS/CSS)'] += loc
                elif 'backend/src/main/java' in rel_path:
                    loc_by_category['Backend (Java/Spring)'] += loc
                elif 'db/migration' in rel_path or 'database/' in rel_path:
                    loc_by_category['Database (SQL)'] += loc
                elif 'scripts/' in rel_path:
                    loc_by_category['Scripts & Tools (Python)'] += loc
                else:
                    loc_by_category['Documentation & Configs'] += loc

    return total_files, total_loc, file_counts, file_locs, loc_by_category

def main():
    project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    print("=" * 70)
    print(" SKYNOVA AIRWAYS -- SOURCE CODE LINES OF CODE (LOC) AUDIT REPORT ")
    print("=" * 70)
    print(f"Project Directory: {project_root}\n")

    total_files, total_loc, file_counts, file_locs, loc_by_category = scan_project(project_root)

    print("CATEGORY BREAKDOWN:")
    print("-" * 50)
    for category, loc in loc_by_category.items():
        print(f"  {category:<30}: {loc:>8,d} LOC")
    print("-" * 50)
    print(f"  {'TOTAL MEANINGFUL LOC':<30}: {total_loc:>8,d} LOC\n")

    print("FILE TYPE BREAKDOWN:")
    print("-" * 50)
    for ext, name in VALID_EXTENSIONS.items():
        if file_counts[ext] > 0:
            print(f"  {name:<25} ({ext:<5}): {file_counts[ext]:>4d} files | {file_locs[ext]:>8,d} LOC")
    print("-" * 50)

    print(f"\nTotal Source Files Audited: {total_files}")
    print(f"Total Meaningful LOC      : {total_loc:,d}")
    print(f"Target Requirement        : 60,000+ LOC")
    
    if total_loc >= 60000:
        print("\nSTATUS: PASS [OK] - Source code meets the 60,000+ LOC requirement!")
    else:
        print(f"\nSTATUS: IN PROGRESS - Currently at {total_loc:,d} / 60,000 LOC.")
        
    print("=" * 70)

if __name__ == '__main__':
    main()
