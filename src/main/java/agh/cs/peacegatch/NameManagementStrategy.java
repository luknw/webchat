package agh.cs.peacegatch;

/**
 * PeaceGatch
 * Created by luknw on 01.02.2017
 */

public interface NameManagementStrategy {
    boolean addName(String name);

    boolean removeName(String name);

    boolean isValid(String name);
}
