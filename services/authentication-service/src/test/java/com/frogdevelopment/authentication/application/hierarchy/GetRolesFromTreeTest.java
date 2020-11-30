package com.frogdevelopment.authentication.application.hierarchy;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class GetRolesFromTreeTest {

    private final GetRolesFromTree getRolesFromTree = new GetRolesFromTree();

    @Test
    void should() {
        // given
        var node_0_0 = HierarchyNode.builder()
                .key("0-0")
                .checked(false)
                .build();
        var node_0_1 = HierarchyNode.builder()
                .key("0-1")
                .checked(true)
                .build();
        var node_0 = HierarchyNode.builder()
                .key("0")
                .checked(false)
                .children(List.of(node_0_0, node_0_1))
                .build();
        var node_1_0_0 = HierarchyNode.builder()
                .key("1-0-0")
                .checked(true)
                .build();
        var node_1_0 = HierarchyNode.builder()
                .key("1-0")
                .checked(true)
                .children(List.of(node_1_0_0))
                .build();
        var node_1 = HierarchyNode.builder()
                .key("1")
                .checked(true)
                .children(List.of(node_1_0))
                .build();
        var nodes = List.of(node_0, node_1);

        // when
        var roles = getRolesFromTree.call(nodes);

        // then
        assertThat(roles).containsOnly("1", "0-1");
    }

}
