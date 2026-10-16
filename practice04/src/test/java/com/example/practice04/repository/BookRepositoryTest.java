@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class BookRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16");

    @Autowired BookRepository repo;

    @Test
    void flywaySeedIsApplied() {
        assertEquals(100, repo.count());
    }

    @Test
    void pagingAndSorting() {
        Page<Book> p = repo.findAll(PageRequest.of(1, 10, Sort.by("price").descending()));
        assertEquals(10, p.getContent().size());
        assertEquals(10, p.getTotalPages());
        assertEquals(1, p.getNumber());
    }

    @Test
    void findByGenre() {
        Page<Book> p = repo.findByGenreName("Роман", PageRequest.of(0, 5));
        assertFalse(p.isEmpty());
        assertTrue(p.getContent().stream().allMatch(b -> b.getGenre().getName().equals("Роман")));
    }

    @Test
    void projectionWorks() {
        Page<BookSummary> p = repo.findAllBy(PageRequest.of(0, 3));
        assertNotNull(p.getContent().get(0).getTitle());
    }
}