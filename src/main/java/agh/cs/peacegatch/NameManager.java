package agh.cs.peacegatch;

/*
  PeaceGatch
  Created by luknw on 01.02.2017
 */

/**
 * Aggregates names meeting certain validity criteria
 */
public interface NameManager {
    /**
     * Adds the name to the underlying name collection if it meets validity criteria.
     * If the collection already contains the name, result is implementation defined.
     *
     * @param name name to add
     * @return {@code true} if the name meets validity criteria and was successfully added, {@code false} otherwise
     */
    boolean addName(Name name);

    /**
     * Removes the name from the underlying name collection.
     * The collection is not modified if it doesn't contain specified name.
     * Handling multiple occurrences is implementation defined.
     *
     * @param name name to remove
     * @return {@code true} if the name was successfully removed,
     * {@code false} if the name collection didn't contain the specified name
     */
    boolean removeName(Name name);

    /**
     * Checks if the name meets internal validity criteria
     *
     * @param name name to check
     * @return {@code true} if the name meets criteria, false otherwise
     */
    boolean isValid(Name name);
}
