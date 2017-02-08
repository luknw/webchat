package agh.cs.peacegatch;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Set;

/**
 * PeaceGatch
 * Created by luknw on 01.02.2017
 */

public class UniqueNameManager implements NameManager {

    private Set<Name> names = new ConcurrentHashSet<>();

    @Override
    public boolean addName(Name name) {
        if (names.contains(name)) {
            return false;
        }

        names.add(name);
        return true;
    }

    @Override
    public boolean removeName(Name name) {
        return names.remove(name);
    }

    @Override
    public boolean isValid(Name name) {
        return !names.contains(name);
    }
}
