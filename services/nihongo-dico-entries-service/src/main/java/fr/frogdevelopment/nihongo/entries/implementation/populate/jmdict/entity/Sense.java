package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Sense {

    @Singular("field")
    private List<String> field;
    @Singular("pos")
    private List<String> pos;
    @Singular("misc")
    private List<String> misc;
    private String info;
    @Singular("dial")
    private List<String> dial;
    @Singular("gloss")
    private List<Gloss> gloss;
}
