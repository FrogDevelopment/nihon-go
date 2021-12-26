package com.frogdevelopment.nihongo.lessons.api;

import com.frogdevelopment.jwt.JwtProcessTokenFilter;
import com.frogdevelopment.nihongo.lessons.application.ExportLessons;
import com.frogdevelopment.nihongo.lessons.application.migrate.OldMigrateLessons;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Web test to fix")
@WebMvcTest(value = ExportController.class)
@AutoConfigureMockMvc
@SpringJUnitWebConfig
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class ExportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExportLessons exportLessons;
    @MockBean
    private OldMigrateLessons oldMigrateLessons;

    @MockBean
    private JwtProcessTokenFilter jwtProcessTokenFilter;

    @Test
    void should_export() throws Exception {
        // when
        this.mvc.perform(post("/export/1"))
                .andExpect(status().isOk());

        // then
        then(exportLessons).should().call(1);
    }
}
