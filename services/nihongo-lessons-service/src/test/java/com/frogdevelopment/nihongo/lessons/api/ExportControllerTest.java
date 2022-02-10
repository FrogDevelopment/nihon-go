package com.frogdevelopment.nihongo.lessons.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@Disabled("Web test to fix")
@Tag("integrationTest")
@MicronautTest
class ExportControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private ExportLessons exportLessons;
//    @MockBean
//    private OldMigrateLessons oldMigrateLessons;
//
//    @MockBean
//    private JwtProcessTokenFilter jwtProcessTokenFilter;
//
//    @Test
//    void should_export() throws Exception {
//        // when
//        this.mvc.perform(post("/export/1"))
//                .andExpect(status().isOk());
//
//        // then
//        then(exportLessons).should().call(1);
//    }
}
