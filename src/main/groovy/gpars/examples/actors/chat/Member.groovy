package gpars.examples.actors.chat

import groovyx.gpars.actor.DefaultActor

/**
 * A member can send a given message to the chat room
 * is assigned to
 */
class Member extends DefaultActor {

    /* Some phrases to make the chat room a little bit interesting */
    final String[] sillyPhrases = [
        'Hey how are you doing ?' ,
        'fine thanks!!',
        'I saw you yesterday',
        'I had no idea',
        'I dont like it',
        'Really?',
        'What were u thinking ?',
        'Yes',
        'No'
    ]

    String name
    ChatRoom chatRoom

    void act() {
        loop {
            /* To enable some random behavior */
            def isGoingToTalk = new Random().nextBoolean()

            if (isGoingToTalk) {
                chatRoom << sillyPhrases[new Random().nextInt(9)]
                /* Be gentle and let others speak */
                Thread.sleep(1000)
            }
        }
    }

}
