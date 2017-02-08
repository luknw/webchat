package agh.cs.peacegatch;

/*
  PeaceGatch
  Created by luknw on 07.02.2017
 */

/**
 * Messages sent by users
 */
public interface Message {
    /**
     * @return Sender of the Message
     */
    User getSender();

    /**
     * @return Content of the Message
     */
    String getContent();
}
