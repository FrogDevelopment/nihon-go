package com.frogdevelopment.nihongo.lessons.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.nihongo.lessons.application.LessonService;
import com.frogdevelopment.nihongo.lessons.entity.InputDto;
import com.frogdevelopment.nihongo.lessons.entity.Japanese;
import com.frogdevelopment.nihongo.lessons.entity.Translation;

@Disabled("Web test to fix")
@WebMvcTest(value = LessonController.class)
@SpringJUnitWebConfig
@AutoConfigureMockMvc
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LessonService lessonService;
    @MockBean
    private JwtProcessTokenFilter jwtProcessTokenFilter;

    private JacksonTester<List<String>> jsonLessonsInformation;
    private JacksonTester<List<InputDto>> jsonListDto;
    private JacksonTester<List<String>> jsonTags;

    @BeforeEach
    void setup() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void getTotal() throws Exception {
        // given
        var filterType = "filterType";

        given(this.lessonService.getTotal()).willReturn(123);

        // when
        this.mvc.perform(
                get("/total")
                        .param(filterType, filterType)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(123)));
    }

    @Test
    void fetch_should_accept_missing_params() throws Exception {
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

        var pageIndex = 1;
        var pageSize = 1;

        given(this.lessonService.fetch(pageIndex, pageSize, null, null)).willReturn(List.of(dto));

        // when
        this.mvc.perform(
                get("/fetch")
                        .param("pageIndex", String.valueOf(pageIndex))
                        .param("pageSize", String.valueOf(pageSize))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListDto.write(List.of(dto)).getJson()));
    }

    @Test
    void fetch() throws Exception {
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

        var pageIndex = 1;
        var pageSize = 1;
        var sortField = "sortField";
        var sortOrder = "sortOrder";
        var filterType = "filterType";

        given(this.lessonService.fetch(pageIndex, pageSize, sortField, sortOrder)).willReturn(List.of(dto));

        // when
        this.mvc.perform(
                get("/fetch")
                        .param("pageIndex", String.valueOf(pageIndex))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("sortField", sortField)
                        .param("sortOrder", sortOrder)
                        .param("filterType", filterType)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListDto.write(List.of(dto)).getJson()));
    }
}
