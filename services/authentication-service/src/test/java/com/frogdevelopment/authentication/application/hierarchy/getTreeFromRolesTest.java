package com.frogdevelopment.authentication.application.hierarchy;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class getTreeFromRolesTest {

    private static final String HIERARCHY = """
            0 > 0-0
            0-0 > 0-0-0
            0-0 > 0-0-1
            0-0-1 > 0-0-1-0
            0-0-1 > 0-0-1-1
            0-0 > 0-0-2
            0 > 0-1
            0-1 > 0-1-0
            0-1 > 0-1-1
            0 > 0-2
            1 > 1-0 > 1-0-0
            """;

    private final GetTreeFromRoles getHierarchy = new GetTreeFromRoles(HIERARCHY);

    @Test
    void shouldReturnHierarchyAsTree() {
        // when
        var roots = getHierarchy.call(List.of());

        // then
        assertThat(roots).hasSize(2);

        var node_0 = roots.get(0);
        assertThat(node_0.getTitle()).isEqualTo("0");
        assertThat(node_0.getKey()).isEqualTo("0");
        assertThat(node_0.isChecked()).isFalse();
        assertThat(node_0.isExpanded()).isTrue();
        assertThat(node_0.isLeaf()).isFalse();
        assertThat(node_0.getChildren()).hasSize(3);

        var node_0_0 = node_0.getChildren().get(0);
        assertThat(node_0_0.getTitle()).isEqualTo("0-0");
        assertThat(node_0_0.getKey()).isEqualTo("0-0");
        assertThat(node_0_0.isChecked()).isFalse();
        assertThat(node_0_0.isExpanded()).isTrue();
        assertThat(node_0_0.isLeaf()).isFalse();
        assertThat(node_0_0.getChildren()).hasSize(3);

        var node_0_0_0 = node_0_0.getChildren().get(0);
        assertThat(node_0_0_0.getTitle()).isEqualTo("0-0-0");
        assertThat(node_0_0_0.getKey()).isEqualTo("0-0-0");
        assertThat(node_0_0_0.isChecked()).isFalse();
        assertThat(node_0_0_0.isExpanded()).isTrue();
        assertThat(node_0_0_0.isLeaf()).isTrue();
        assertThat(node_0_0_0.getChildren()).isNull();

        var node_0_0_1 = node_0_0.getChildren().get(1);
        assertThat(node_0_0_1.getTitle()).isEqualTo("0-0-1");
        assertThat(node_0_0_1.getKey()).isEqualTo("0-0-1");
        assertThat(node_0_0_1.isChecked()).isFalse();
        assertThat(node_0_0_1.isExpanded()).isTrue();
        assertThat(node_0_0_1.isLeaf()).isFalse();
        assertThat(node_0_0_1.getChildren()).hasSize(2);

        var node_0_0_1_0 = node_0_0_1.getChildren().get(0);
        assertThat(node_0_0_1_0.getTitle()).isEqualTo("0-0-1-0");
        assertThat(node_0_0_1_0.getKey()).isEqualTo("0-0-1-0");
        assertThat(node_0_0_1_0.isChecked()).isFalse();
        assertThat(node_0_0_1_0.isExpanded()).isTrue();
        assertThat(node_0_0_1_0.isLeaf()).isTrue();
        assertThat(node_0_0_1_0.getChildren()).isNull();

        var node_0_0_1_1 = node_0_0_1.getChildren().get(1);
        assertThat(node_0_0_1_1.getTitle()).isEqualTo("0-0-1-1");
        assertThat(node_0_0_1_1.getKey()).isEqualTo("0-0-1-1");
        assertThat(node_0_0_1_1.isChecked()).isFalse();
        assertThat(node_0_0_1_1.isExpanded()).isTrue();
        assertThat(node_0_0_1_1.isLeaf()).isTrue();
        assertThat(node_0_0_1_1.getChildren()).isNull();

        var node_0_0_2 = node_0_0.getChildren().get(2);
        assertThat(node_0_0_2.getTitle()).isEqualTo("0-0-2");
        assertThat(node_0_0_2.getKey()).isEqualTo("0-0-2");
        assertThat(node_0_0_2.isChecked()).isFalse();
        assertThat(node_0_0_2.isExpanded()).isTrue();
        assertThat(node_0_0_2.isLeaf()).isTrue();
        assertThat(node_0_0_2.getChildren()).isNull();

        var node_0_1 = node_0.getChildren().get(1);
        assertThat(node_0_1.getTitle()).isEqualTo("0-1");
        assertThat(node_0_1.getKey()).isEqualTo("0-1");
        assertThat(node_0_1.isChecked()).isFalse();
        assertThat(node_0_1.isExpanded()).isTrue();
        assertThat(node_0_1.isLeaf()).isFalse();
        assertThat(node_0_1.getChildren()).hasSize(2);

        var node_0_1_0 = node_0_1.getChildren().get(0);
        assertThat(node_0_1_0.getTitle()).isEqualTo("0-1-0");
        assertThat(node_0_1_0.getKey()).isEqualTo("0-1-0");
        assertThat(node_0_1_0.isChecked()).isFalse();
        assertThat(node_0_1_0.isExpanded()).isTrue();
        assertThat(node_0_1_0.isLeaf()).isTrue();
        assertThat(node_0_1_0.getChildren()).isNull();

        var node_0_1_1 = node_0_1.getChildren().get(1);
        assertThat(node_0_1_1.getTitle()).isEqualTo("0-1-1");
        assertThat(node_0_1_1.getKey()).isEqualTo("0-1-1");
        assertThat(node_0_1_1.isChecked()).isFalse();
        assertThat(node_0_1_1.isExpanded()).isTrue();
        assertThat(node_0_1_1.isLeaf()).isTrue();
        assertThat(node_0_1_1.getChildren()).isNull();

        var node_0_2 = node_0.getChildren().get(2);
        assertThat(node_0_2.getTitle()).isEqualTo("0-2");
        assertThat(node_0_2.getKey()).isEqualTo("0-2");
        assertThat(node_0_2.isChecked()).isFalse();
        assertThat(node_0_2.isExpanded()).isTrue();
        assertThat(node_0_2.isLeaf()).isTrue();
        assertThat(node_0_2.getChildren()).isNull();

        var node_1 = roots.get(1);
        assertThat(node_1.getTitle()).isEqualTo("1");
        assertThat(node_1.getKey()).isEqualTo("1");
        assertThat(node_1.isChecked()).isFalse();
        assertThat(node_1.isExpanded()).isTrue();
        assertThat(node_1.isLeaf()).isFalse();
        assertThat(node_1.getChildren()).hasSize(1);

        var node_1_0 = node_1.getChildren().get(0);
        assertThat(node_1_0.getTitle()).isEqualTo("1-0");
        assertThat(node_1_0.getKey()).isEqualTo("1-0");
        assertThat(node_1_0.isChecked()).isFalse();
        assertThat(node_1_0.isExpanded()).isTrue();
        assertThat(node_1_0.isLeaf()).isFalse();
        assertThat(node_1_0.getChildren()).hasSize(1);

        var node_1_0_0 = node_1_0.getChildren().get(0);
        assertThat(node_1_0_0.getTitle()).isEqualTo("1-0-0");
        assertThat(node_1_0_0.getKey()).isEqualTo("1-0-0");
        assertThat(node_1_0_0.isChecked()).isFalse();
        assertThat(node_1_0_0.isExpanded()).isTrue();
        assertThat(node_1_0_0.isLeaf()).isTrue();
        assertThat(node_1_0_0.getChildren()).isNull();
    }

    @Test
    void should_0_0_1_fully_checked_and_1_fully_checked() {
        // when
        var roots = getHierarchy.call(List.of("0-0-1", "1"));

        // then
        var node_0 = roots.get(0);
        assertThat(node_0.isChecked()).isFalse();

        var node_0_0 = node_0.getChildren().get(0);
        assertThat(node_0_0.isChecked()).isFalse();

        var node_0_0_0 = node_0_0.getChildren().get(0);
        assertThat(node_0_0_0.isChecked()).isFalse();

        var node_0_0_1 = node_0_0.getChildren().get(1);
        assertThat(node_0_0_1.isChecked()).isTrue();

        var node_0_0_1_0 = node_0_0_1.getChildren().get(0);
        assertThat(node_0_0_1_0.isChecked()).isTrue();

        var node_0_0_1_1 = node_0_0_1.getChildren().get(1);
        assertThat(node_0_0_1_1.isChecked()).isTrue();

        var node_0_0_2 = node_0_0.getChildren().get(2);
        assertThat(node_0_0_2.isChecked()).isFalse();

        var node_0_1 = node_0.getChildren().get(1);
        assertThat(node_0_1.isChecked()).isFalse();

        var node_0_1_0 = node_0_1.getChildren().get(0);
        assertThat(node_0_1_0.isChecked()).isFalse();

        var node_0_1_1 = node_0_1.getChildren().get(1);
        assertThat(node_0_1_1.isChecked()).isFalse();

        var node_0_2 = node_0.getChildren().get(2);
        assertThat(node_0_2.isChecked()).isFalse();

        var node_1 = roots.get(1);
        assertThat(node_1.isChecked()).isTrue();

        var node_1_0 = node_1.getChildren().get(0);
        assertThat(node_1_0.isChecked()).isTrue();

        var node_1_0_0 = node_1_0.getChildren().get(0);
        assertThat(node_1_0_0.isChecked()).isTrue();
    }

    @Test
    void should_0_fully_checked_and_1_fully_not_checked() {
        // when
        var roots = getHierarchy.call(List.of("0"));

        // then
        var node_0 = roots.get(0);
        assertThat(node_0.isChecked()).isTrue();

        var node_0_0 = node_0.getChildren().get(0);
        assertThat(node_0_0.isChecked()).isTrue();

        var node_0_0_0 = node_0_0.getChildren().get(0);
        assertThat(node_0_0_0.isChecked()).isTrue();

        var node_0_0_1 = node_0_0.getChildren().get(1);
        assertThat(node_0_0_1.isChecked()).isTrue();

        var node_0_0_1_0 = node_0_0_1.getChildren().get(0);
        assertThat(node_0_0_1_0.isChecked()).isTrue();

        var node_0_0_1_1 = node_0_0_1.getChildren().get(1);
        assertThat(node_0_0_1_1.isChecked()).isTrue();

        var node_0_0_2 = node_0_0.getChildren().get(2);
        assertThat(node_0_0_2.isChecked()).isTrue();

        var node_0_1 = node_0.getChildren().get(1);
        assertThat(node_0_1.isChecked()).isTrue();
        var node_0_1_0 = node_0_1.getChildren().get(0);
        assertThat(node_0_1_0.isChecked()).isTrue();

        var node_0_1_1 = node_0_1.getChildren().get(1);
        assertThat(node_0_1_1.isChecked()).isTrue();

        var node_0_2 = node_0.getChildren().get(2);
        assertThat(node_0_2.isChecked()).isTrue();

        var node_1 = roots.get(1);
        assertThat(node_1.isChecked()).isFalse();

        var node_1_0 = node_1.getChildren().get(0);
        assertThat(node_1_0.isChecked()).isFalse();

        var node_1_0_0 = node_1_0.getChildren().get(0);
        assertThat(node_1_0_0.isChecked()).isFalse();
    }

}
