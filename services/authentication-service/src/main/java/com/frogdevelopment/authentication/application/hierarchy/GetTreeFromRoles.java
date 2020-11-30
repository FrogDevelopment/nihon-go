package com.frogdevelopment.authentication.application.hierarchy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static java.util.stream.Stream.ofNullable;

@Component
@RefreshScope
public class GetTreeFromRoles {

    private List<HierarchyNode> tree;

    public GetTreeFromRoles(@Value("${security.hierarchy}") String hierarchyRoles) {
        processHierarchyToTree(hierarchyRoles);
    }

    private void processHierarchyToTree(String hierarchyRoles) {
        var notRoots = new HashSet<>();
        var mapHierarchies = new HashMap<String, List<String>>();
        of(hierarchyRoles.split("\n"))
                .forEach(hierarchy -> {
                    // Split on > and trim excessive whitespace
                    var roles = hierarchy.trim().split("\\s+>\\s+");

                    for (var i = 1; i < roles.length; i++) {
                        var higherRole = roles[i - 1];
                        var lowerRole = roles[i];

                        notRoots.add(lowerRole);
                        mapHierarchies.computeIfAbsent(higherRole, k -> new ArrayList<>()).add(lowerRole);
                    }
                });

        var mapHierarchyNodes = new HashMap<String, HierarchyNode>();
        mapHierarchies.forEach((key, value) -> mapHierarchyNodes.computeIfAbsent(key, GetTreeFromRoles::toNode)
                .setChildren(value.stream()
                        .map(keyChild -> mapHierarchyNodes.computeIfAbsent(keyChild, GetTreeFromRoles::toNode))
                        .collect(Collectors.toList())
                ));

        tree = mapHierarchyNodes.values().stream()
                .peek(n -> n.setLeaf(CollectionUtils.isEmpty(n.getChildren())))
                .filter(n -> !notRoots.contains(n.getKey()))
                .collect(Collectors.toList());
    }

    public List<HierarchyNode> call(List<String> currentRoles) {
        var parent = new ArrayList<>(tree);

        checkNodeIfHasRole(currentRoles, parent);

        return parent;
    }

    private void checkNodeIfHasRole(List<String> currentRoles, Collection<HierarchyNode> nodes) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    if (currentRoles.contains(node.getKey())) {
                        node.setChecked(true);
                        checkChildren(node.getChildren());
                    } else {
                        checkNodeIfHasRole(currentRoles, node.getChildren());
                    }
                });
    }

    private void checkChildren(Collection<HierarchyNode> nodes) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    node.setChecked(true);
                    checkChildren(node.getChildren());
                });
    }

    private static HierarchyNode toNode(String key) {
        return HierarchyNode.builder()
                .title(key)
                .key(key)
                .build();
    }

}
