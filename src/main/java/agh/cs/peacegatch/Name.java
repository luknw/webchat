package agh.cs.peacegatch;

/*
  PeaceGatch
  Created by luknw on 08.02.2017
 */

/**
 * String wrapper representing some name, e.g. user's or channel's.
 * Implementations have to override toString() to return the underlying value.
 */
public interface Name {
    /**
     * @return Underlying name value
     */
    String getName();
}
