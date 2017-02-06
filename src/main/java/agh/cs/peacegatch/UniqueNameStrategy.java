package agh.cs.peacegatch;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Set;

/**
 * PeaceGatch
 * Created by luknw on 01.02.2017
 */

public class UniqueNameStrategy implements NameManagementStrategy {

    private Set<String> names = new ConcurrentHashSet<>();

    @Override
    public boolean addName(String name) {
        if (names.contains(name)) {
            return false;
        }

        names.add(name);
        return true;
    }

    @Override
    public boolean removeName(String name) {
        return names.remove(name);
    }

    @Override
    public boolean isValid(String name) {
        return !names.contains(name);
    }
}
