package fr.frogdevelopment.nihongo.lesson.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import fr.frogdevelopment.nihongo.lesson.entity.InputDto;
import fr.frogdevelopment.nihongo.lesson.entity.Japanese;
import fr.frogdevelopment.nihongo.lesson.entity.Translation;
import fr.frogdevelopment.nihongo.lesson.implementation.CreateLesson;
import fr.frogdevelopment.nihongo.lesson.implementation.DeleteLesson;
import fr.frogdevelopment.nihongo.lesson.implementation.UpdateLesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Disabled("Web test to fix")
@WebMvcTest(value = LessonAdminController.class)
@AutoConfigureMockMvc
@SpringJUnitWebConfig
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class LessonAdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateLesson createLesson;
    @MockBean
    private UpdateLesson updateLesson;
    @MockBean
    private DeleteLesson deleteLesson;

    @MockBean
    private JwtProcessTokenFilter jwtProcessTokenFilter;

    private JacksonTester<InputDto> jsonDto;

    @BeforeEach
    void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void insert() throws Exception {
        // given
        var dto = InputDto.builder()
                .japanese(Japanese.builder()
                        .kanji("私")
                        .kana("わたし")
                        .build())
                .translation(Translation.builder()
                        .locale("fr_FR")
                        .input("Je, Moi")
                        .sortLetter("J")
                        .details("détails")
                        .example("exemple")
                        .tag("tag_1")
                        .tag("tag_2")
                        .build())
                .build();

        given(this.createLesson.call(dto)).will(invocation -> {
            dto.getJapanese().setId(987);
            dto.getTranslations().get(0).setId(654);

            return dto;
        });

        // when
        this.mvc.perform(
                post("/admin")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonDto.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDto.write(dto).getJson()));
    }

    @Test
    void update() throws Exception {
        // given
        var dto = InputDto.builder()
                .japanese(Japanese.builder()
                        .id(123)
                        .kanji("私")
                        .kana("わたし")
                        .build())
                .translation(Translation.builder()
                        .id(465)
                        .japaneseId(123)
                        .locale("fr_FR")
                        .input("Je, Moi")
                        .sortLetter("J")
                        .details("détails")
                        .example("exemple")
                        .tag("tag_1")
                        .tag("tag_2")
                        .build())
                .build();

        given(this.updateLesson.call(dto)).willReturn(dto);

        // when
        this.mvc.perform(
                put("/admin")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonDto.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDto.write(dto).getJson()));
    }

    @Test
    void delete() throws Exception {
        // given
        var dto = InputDto.builder()
                .japanese(Japanese.builder()
                        .id(123)
                        .kanji("私")
                        .kana("わたし")
                        .build())
                .translation(Translation.builder()
                        .id(465)
                        .japaneseId(123)
                        .locale("fr_FR")
                        .input("Je, Moi")
                        .sortLetter("J")
                        .details("détails")
                        .example("exemple")
                        .tag("tag_1")
                        .tag("tag_2")
                        .build())
                .build();

        // when
        this.mvc.perform(
                MockMvcRequestBuilders.delete("/admin")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonDto.write(dto).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        BDDMockito.then(deleteLesson).should().call(dto);
    }
}
