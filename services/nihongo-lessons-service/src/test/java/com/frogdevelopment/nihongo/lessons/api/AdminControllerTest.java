package com.frogdevelopment.nihongo.lessons.api;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.nihongo.lessons.application.ManageLessons;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Disabled("Web test to fix")
@WebMvcTest(value = AdminController.class)
@AutoConfigureMockMvc
@SpringJUnitWebConfig
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ManageLessons manageLessons;

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
                        .sortLetter('J')
                        .details("détails")
                        .example("exemple")
                        .build())
                .build();

        given(manageLessons.insert(dto)).will(invocation -> {
//            dto.getJapanese().setId(987);
//            dto.getTranslations().get(0).setId(654);

            return dto;
        });

        // when
        this.mvc.perform(
                post("/admin")
                        .contentType(APPLICATION_JSON)
                        .content(jsonDto.write(dto).getJson())
                        .accept(APPLICATION_JSON))
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
                        .sortLetter('J')
                        .details("détails")
                        .example("exemple")
                        .build())
                .build();

        given(manageLessons.update(dto)).willReturn(dto);

        // when
        this.mvc.perform(
                put("/admin")
                        .contentType(APPLICATION_JSON)
                        .content(jsonDto.write(dto).getJson())
                        .accept(APPLICATION_JSON))
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
                        .sortLetter('J')
                        .details("détails")
                        .example("exemple")
                        .build())
                .build();

        // when
        this.mvc.perform(
                MockMvcRequestBuilders.delete("/admin")
                        .contentType(APPLICATION_JSON)
                        .content(jsonDto.write(dto).getJson())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        then(manageLessons).should().delete(dto);
    }
}
