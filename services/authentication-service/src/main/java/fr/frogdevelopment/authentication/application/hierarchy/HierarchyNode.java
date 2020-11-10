package fr.frogdevelopment.authentication.application.hierarchy;

import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class HierarchyNode {

    private String title;
    private String key;
    private boolean checked;
    @Default
    private boolean expanded = true;
    private boolean leaf;
    @Default
    private List<HierarchyNode> children = null;
}
