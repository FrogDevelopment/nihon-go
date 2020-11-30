package com.frogdevelopment.authentication.application.hierarchy;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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
