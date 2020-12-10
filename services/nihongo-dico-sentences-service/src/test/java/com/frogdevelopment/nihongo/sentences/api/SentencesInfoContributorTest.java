package com.frogdevelopment.nihongo.sentences.api;

import com.frogdevelopment.nihongo.sentences.implementation.about.GetAbout;
import com.frogdevelopment.nihongo.sentences.info.SentencesInfoContributor;
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
class SentencesInfoContributorTest {

    @InjectMocks
    private SentencesInfoContributor sentencesInfoContributor;

    @Mock
    private GetAbout getAbout;

    @Test
    void shouldCall() {
        // given
        Info.Builder builder = new Info.Builder();
        String about = "jsonAbout";
        given(getAbout.call()).willReturn(about);

        // when
        sentencesInfoContributor.contribute(builder);

        // then
        assertThat(builder.build().get("about")).isEqualTo(about);
    }

}
