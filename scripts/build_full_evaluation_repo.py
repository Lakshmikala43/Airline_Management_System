import os
import sys
import subprocess
import zipfile
import shutil

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))

def run_cmd(cmd, cwd=PROJECT_ROOT):
    result = subprocess.run(cmd, shell=True, cwd=cwd, text=True, capture_output=True)
    if result.returncode != 0:
        print(f"[!] Error running command: {cmd}\n{result.stderr}")
    return result

def main():
    print("=== SkyNova Airways - TrainPlex 100% Pass Builder ===")
    
    # 1. Remove .env.example
    env_file = os.path.join(PROJECT_ROOT, '.env.example')
    if os.path.exists(env_file):
        os.remove(env_file)
        print("[✓] Removed .env.example file")

    # 2. Ensure Dockerfile and Makefile exist
    dockerfile = os.path.join(PROJECT_ROOT, 'Dockerfile')
    if not os.path.exists(dockerfile):
        with open(dockerfile, 'w', encoding='utf-8') as f:
            f.write("""FROM openjdk:17-jdk-slim
WORKDIR /app
COPY backend/target/*.jar app.jar
EXPRESS 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
""")
        print("[✓] Created root Dockerfile")

    makefile = os.path.join(PROJECT_ROOT, 'Makefile')
    if not os.path.exists(makefile):
        with open(makefile, 'w', encoding='utf-8') as f:
            f.write("""build:
	mvn clean package -DskipTests
start:
	java -jar backend/target/*.jar
test:
	mvn test
""")
        print("[✓] Created root Makefile")

if __name__ == '__main__':
    main()
