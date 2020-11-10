package fr.frogdevelopment.authentication.application.hierarchy;

import static java.util.stream.Stream.ofNullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GetRolesFromTree {

    public List<String> call(List<HierarchyNode> nodes) {
        var roles = new ArrayList<String>();
        getCheckedRoles(nodes, roles);

        return roles;
    }

    private void getCheckedRoles(List<HierarchyNode> nodes, ArrayList<String> roles) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    if (node.isChecked()) {
                        roles.add(node.getKey());
                    } else {
                        getCheckedRoles(node.getChildren(), roles);
                    }
                });
    }

}
