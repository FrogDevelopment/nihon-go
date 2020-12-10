package com.frogdevelopment.nihongo.sentences.implementation.about;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class GetAboutTest {

    @InjectMocks
    private GetAbout getAbout;

    @Mock
    private AboutDao aboutDao;

    @Test
    void shouldCallDao() {
        // when
        getAbout.call();

        // then
        then(aboutDao).should().getLast();
    }

}
