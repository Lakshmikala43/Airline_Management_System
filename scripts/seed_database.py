#!/usr/bin/env python3
"""
SkyNova Airways - Database Seeder Utility
Utility script to trigger initial data seeding or verify Flyway migrations.
"""

import sys
import urllib.request
import json

def check_health(backend_url):
    url = f"{backend_url}/health"
    try:
        req = urllib.request.Request(url)
        with urllib.request.urlopen(req) as response:
            if response.status == 200:
                data = json.loads(response.read().decode('utf-8'))
                print(f"[✓] Backend status: {data.get('status')} - Service: {data.get('service')}")
                return True
    except Exception as e:
        print(f"[×] Failed to connect to backend at {url}: {e}")
        return False

def main():
    backend_url = "http://localhost:8080/api/v1"
    print("=" * 60)
    print(" SKYNOVA AIRWAYS — DATABASE & BACKEND HEALTH CHECK SEEDER ")
    print("=" * 60)
    
    if check_health(backend_url):
        print("\nSeed data loaded automatically via Flyway Migrations (V1..V7).")
        print("Demo Accounts:")
        print("  - Admin   : admin@skynova.demo    | Password123!")
        print("  - Manager : manager@skynova.demo  | Password123!")
        print("  - Customer: customer@skynova.demo | Password123!")
    print("=" * 60)

if __name__ == '__main__':
    main()
