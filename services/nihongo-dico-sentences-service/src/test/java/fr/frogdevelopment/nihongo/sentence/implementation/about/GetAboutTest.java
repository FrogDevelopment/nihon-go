package fr.frogdevelopment.nihongo.sentence.implementation.about;

import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
