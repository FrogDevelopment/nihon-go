package com.frogdevelopment.nihongo.lessons;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("test")
@Tag("integrationTest")
@ExtendWith(SpringExtension.class)
class LessonsApplicationTest {

    @Test
    void contextLoads() {
    }

}
