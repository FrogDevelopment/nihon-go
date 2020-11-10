package fr.frogdevelopment.nihongo.lesson.entity;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Japanese japanese;
    @Singular("translation")
    private List<Translation> translations;
}
