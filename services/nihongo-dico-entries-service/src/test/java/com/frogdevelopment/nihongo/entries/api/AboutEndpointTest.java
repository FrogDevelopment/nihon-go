package com.frogdevelopment.nihongo.entries.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.info.AboutEndpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class AboutEndpointTest {

    @InjectMocks
    private AboutEndpoint aboutEndpoint;

    @Mock
    private AboutDao aboutDao;

    @Test
    void shouldCall() {
        // given
        given(aboutDao.getLast()).willReturn("jsonAbout");

        // when
        var about = aboutEndpoint.read();

        // then
        assertThat(about).isEqualTo("jsonAbout");
    }

}
