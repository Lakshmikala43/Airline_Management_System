#!/usr/bin/env python3
"""
SkyNova Airways - TrainPlex & LOC Measurement Script
Callable by TrainPlex evaluator via: python measure.py
Outputs JSON lines of code count and audits source code.
"""

import os
import sys
import json

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

def scan_project(root_dir):
    loc_by_category = {
        'Frontend (React/TS/CSS)': 0,
        'Backend (Java/Spring)': 0,
        'Database (SQL)': 0,
        'Automated Tests': 0,
        'Scripts & Tools (Python)': 0,
        'Documentation & Configs': 0
    }
    
    total_files = 0
    total_loc = 0

    for dirpath, dirnames, filenames in os.walk(root_dir):
        dirnames[:] = [d for d in dirnames if d not in EXCLUDE_DIRS]
        
        for filename in filenames:
            ext = os.path.splitext(filename)[1].lower()
            if ext in VALID_EXTENSIONS:
                filepath = os.path.join(dirpath, filename)
                loc = 0
                try:
                    with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
                        for line in f:
                            if is_meaningful_line(line, ext):
                                loc += 1
                except Exception:
                    pass

                total_files += 1
                total_loc += loc
                
                rel_path = os.path.relpath(filepath, root_dir).replace('\\', '/')
                if 'src/test' in rel_path or 'frontend/tests' in rel_path or 'tests/' in rel_path:
                    loc_by_category['Automated Tests'] += loc
                elif 'frontend/src' in rel_path:
                    loc_by_category['Frontend (React/TS/CSS)'] += loc
                elif 'backend/src/main/java' in rel_path:
                    loc_by_category['Backend (Java/Spring)'] += loc
                elif 'db/migration' in rel_path or 'database/' in rel_path:
                    loc_by_category['Database (SQL)'] += loc
                elif 'scripts/' in rel_path or rel_path.endswith('.py'):
                    loc_by_category['Scripts & Tools (Python)'] += loc
                else:
                    loc_by_category['Documentation & Configs'] += loc

    return total_files, total_loc, loc_by_category

def main():
    project_root = os.path.abspath(os.path.dirname(__file__))
    total_files, total_loc, loc_by_category = scan_project(project_root)

    result = {
        "status": "PASS" if total_loc >= 60000 else "IN_PROGRESS",
        "total_meaningful_loc": total_loc,
        "target_loc_requirement": 60000,
        "total_files_audited": total_files,
        "category_breakdown": loc_by_category
    }

    # Print JSON output for TrainPlex collectors
    print(json.dumps(result, indent=2))

    # Also print human-readable summary
    print("\n" + "=" * 65)
    print(" SKYNOVA AIRWAYS -- TRAINPLEX MEASURE.PY AUDIT REPORT ")
    print("=" * 65)
    print(f"Total Meaningful LOC : {total_loc:,d}")
    print(f"Total Files Audited  : {total_files:,d}")
    print(f"Audit Status         : {'PASS [OK]' if total_loc >= 60000 else 'IN_PROGRESS'}")
    print("=" * 65)

if __name__ == '__main__':
    main()
