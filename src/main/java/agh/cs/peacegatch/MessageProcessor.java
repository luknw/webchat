package agh.cs.peacegatch;

/*
  PeaceGatch
  Created by luknw on 07.02.2017
 */

/**
 * Performs actions based on message content
 */
public interface MessageProcessor {
    /**
     * Perform actions based on Message content
     *
     * @param message message to process
     */
    void processMessage(Message message);
}
