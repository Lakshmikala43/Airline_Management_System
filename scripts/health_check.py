#!/usr/bin/env python3
"""
SkyNova Airways - Automated System Health Check Script
Verifies Backend API Health (/api/v1/health) and database connectivity.
"""

import sys
import urllib.request
import json

def run_health_check():
    health_url = "http://localhost:8080/api/v1/health"
    print(f"Checking health at {health_url}...")
    try:
        with urllib.request.urlopen(health_url) as resp:
            if resp.status == 200:
                body = json.loads(resp.read().decode('utf-8'))
                print(f"HEALTH STATUS: {body.get('status')} [UP]")
                print(f"SERVICE NAME : {body.get('service')}")
                return 0
            else:
                print(f"HEALTH CHECK FAILED: HTTP {resp.status}")
                return 1
    except Exception as e:
        print(f"ERROR connecting to backend health endpoint: {e}")
        print("Note: Ensure Spring Boot backend is running via './mvnw spring-boot:run'")
        return 1

if __name__ == '__main__':
    sys.exit(run_health_check())
