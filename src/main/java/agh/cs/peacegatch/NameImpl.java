package agh.cs.peacegatch;

/**
 * PeaceGatch
 * Created by luknw on 08.02.2017
 */

public class NameImpl implements Name {
    private final String name;

    public NameImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameImpl other = (NameImpl) o;

        return name != null ? name.equals(other.name) : other.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
