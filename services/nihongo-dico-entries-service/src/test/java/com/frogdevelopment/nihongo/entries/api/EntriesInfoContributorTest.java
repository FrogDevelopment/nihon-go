package com.frogdevelopment.nihongo.entries.api;

import com.frogdevelopment.nihongo.entries.implementation.about.AboutDao;
import com.frogdevelopment.nihongo.entries.info.EntriesInfoContributor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.info.Info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class EntriesInfoContributorTest {

    @InjectMocks
    private EntriesInfoContributor entriesInfoContributor;

    @Mock
    private AboutDao aboutDao;

    @Test
    void shouldCall() {
        // given
        var builder = new Info.Builder();
        var about = "jsonAbout";
        given(aboutDao.getLast()).willReturn(about);

        // when
        entriesInfoContributor.contribute(builder);

        // then
        var info = builder.build();
        assertThat(info.get("about")).isEqualTo(about);
    }

}
