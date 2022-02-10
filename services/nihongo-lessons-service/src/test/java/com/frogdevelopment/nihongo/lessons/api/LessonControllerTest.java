package com.frogdevelopment.nihongo.lessons.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@Disabled("Web test to fix")
@Tag("integrationTest")
@MicronautTest
class LessonControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private LessonService lessonService;
//    @MockBean
//    private ManageLessons manageLessons;
//
//    @MockBean
//    private JwtProcessTokenFilter jwtProcessTokenFilter;
//
//    private JacksonTester<List<InputDto>> jsonListDto;
//    private JacksonTester<InputDto> jsonDto;
//
//    @BeforeEach
//    void setup() {
//        var objectMapper = new ObjectMapper();
//        JacksonTester.initFields(this, objectMapper);
//    }
//
//    @Test
//    void fetch_should_accept_missing_params() throws Exception {
//        // given
//        var dto = InputDto.builder()
//                .japanese(Japanese.builder()
//                        .id(123)
//                        .kanji("私")
//                        .kana("わたし")
//                        .build())
//                .translation("fr_FR", Translation.builder()
//                        .id(465)
//                        .japaneseId(123)
//                        .locale("fr_FR")
//                        .input("Je, Moi")
//                        .sortLetter('J')
//                        .details("détails")
//                        .example("exemple")
//                        .build())
//                .build();
//
//        var lesson = 1;
//
//        given(this.lessonService.fetch(lesson, null, null)).willReturn(List.of(dto));
//
//        // when
//        this.mvc.perform(
//                get("/fetch")
//                        .param("lesson", String.valueOf(lesson))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(jsonListDto.write(List.of(dto)).getJson()));
//    }
//
//    @Test
//    void fetch() throws Exception {
//        // given
//        var dto = InputDto.builder()
//                .japanese(Japanese.builder()
//                        .id(123)
//                        .kanji("私")
//                        .kana("わたし")
//                        .build())
//                .translation("fr_FR", Translation.builder()
//                        .id(465)
//                        .japaneseId(123)
//                        .locale("fr_FR")
//                        .input("Je, Moi")
//                        .sortLetter('J')
//                        .details("détails")
//                        .example("exemple")
//                        .build())
//                .build();
//
//        var lesson = 1;
//        var sortField = "sortField";
//        var sortOrder = "sortOrder";
//        var filterType = "filterType";
//
//        given(this.lessonService.fetch(lesson, sortField, sortOrder)).willReturn(List.of(dto));
//
//        // when
//        this.mvc.perform(
//                get("/fetch")
//                        .param("lesson", String.valueOf(lesson))
//                        .param("sortField", sortField)
//                        .param("sortOrder", sortOrder)
//                        .param("filterType", filterType)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(jsonListDto.write(List.of(dto)).getJson()));
//    }
//
//    @Test
//    void insert() throws Exception {
//        // given
//        var dto = InputDto.builder()
//                .japanese(Japanese.builder()
//                        .kanji("私")
//                        .kana("わたし")
//                        .build())
//                .translation("fr_FR", Translation.builder()
//                        .locale("fr_FR")
//                        .input("Je, Moi")
//                        .sortLetter('J')
//                        .details("détails")
//                        .example("exemple")
//                        .build())
//                .build();
//
//        given(manageLessons.insert(dto)).will(invocation -> {
////            dto.getJapanese().setId(987);
////            dto.getTranslations().get(0).setId(654);
//
//            return dto;
//        });
//
//        // when
//        this.mvc.perform(
//                        post("/admin")
//                                .contentType(APPLICATION_JSON)
//                                .content(jsonDto.write(dto).getJson())
//                                .accept(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(jsonDto.write(dto).getJson()));
//    }
//
//    @Test
//    void update() throws Exception {
//        // given
//        var dto = InputDto.builder()
//                .japanese(Japanese.builder()
//                        .id(123)
//                        .kanji("私")
//                        .kana("わたし")
//                        .build())
//                .translation("fr_FR", Translation.builder()
//                        .id(465)
//                        .japaneseId(123)
//                        .locale("fr_FR")
//                        .input("Je, Moi")
//                        .sortLetter('J')
//                        .details("détails")
//                        .example("exemple")
//                        .build())
//                .build();
//
//        given(manageLessons.update(dto)).willReturn(dto);
//
//        // when
//        this.mvc.perform(
//                        put("/admin")
//                                .contentType(APPLICATION_JSON)
//                                .content(jsonDto.write(dto).getJson())
//                                .accept(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(jsonDto.write(dto).getJson()));
//    }
//
//    @Test
//    void delete() throws Exception {
//        // given
//        var dto = InputDto.builder()
//                .japanese(Japanese.builder()
//                        .id(123)
//                        .kanji("私")
//                        .kana("わたし")
//                        .build())
//                .translation("fr_FR", Translation.builder()
//                        .id(465)
//                        .japaneseId(123)
//                        .locale("fr_FR")
//                        .input("Je, Moi")
//                        .sortLetter('J')
//                        .details("détails")
//                        .example("exemple")
//                        .build())
//                .build();
//
//        // when
//        this.mvc.perform(
//                        MockMvcRequestBuilders.delete("/admin")
//                                .contentType(APPLICATION_JSON)
//                                .content(jsonDto.write(dto).getJson())
//                                .accept(APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // then
//        then(manageLessons).should().delete(dto);
//    }
}
