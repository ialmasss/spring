# SIS 2 — layered architecture and test coverage

Student registry: Spring Boot 3.4, Java 21, PostgreSQL, Flyway, MapStruct.

## Layers and models

    HTTP JSON ──► web           StudentController, StudentRequest / StudentResponse (DTO)
                   │  StudentWebMapper        DTO  <-> domain
                   ▼
                 domain/service  Student (domain record), StudentService (@Transactional), exceptions
                   │  StudentEntityMapper     domain <-> entity
                   ▼
                 persistence     StudentEntity (JPA), StudentJpaRepository (Spring Data)
                   ▼
                 PostgreSQL      schema owned by Flyway (db/migration)

Dependencies point inwards: the controller never sees entities, the repository never sees DTOs.

## Tests

| Layer | Class | Tools |
|---|---|---|
| service | StudentServiceTest | JUnit 5, Mockito, ArgumentCaptor, AssertJ |
| web | StudentControllerTest | @WebMvcTest, MockMvc, @MockitoBean |
| repository | StudentJpaRepositoryIT | @DataJpaTest, Testcontainers PostgreSQL, Flyway |

## Commands

    mvn test                # unit + web slice tests
    mvn verify              # + integration test (Docker needed) + JaCoCo report
    open target/site/jacoco/index.html

    docker compose up -d && mvn spring-boot:run   # run the app locally
