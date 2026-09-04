# Contributing Guidelines — SkyNova Airways

Thank you for contributing to the SkyNova Airways project!

## Development Guidelines

1. **Zero Secret Policy**:
   - NEVER commit real credentials, database passwords, or private tokens.
   - Always update `.env.example` when introducing new environment configuration keys.

2. **Code Quality & Architecture**:
   - Follow SOLID principles and clean layered architecture: `Controller -> Service -> Repository -> Database`.
   - Never expose JPA Entities directly via REST API endpoints; always use DTOs and Mappers.
   - Ensure explicit error handling via `GlobalExceptionHandler`.

3. **LOC Audit Verification**:
   - Before submitting pull requests, run `python scripts/count_loc.py` to ensure project source code integrity.

4. **Testing Requirements**:
   - Run backend unit/integration tests: `./mvnw test`
   - Run frontend tests: `npm test`
