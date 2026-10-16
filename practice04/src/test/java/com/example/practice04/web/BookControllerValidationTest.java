@WebMvcTest(BookController.class)
class BookControllerValidationTest {
    @Autowired MockMvc mvc;
    @MockBean BookService service;

    @Test
    void invalidBodyReturns400() throws Exception {
        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"isbn\":\"123\",\"price\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }
}