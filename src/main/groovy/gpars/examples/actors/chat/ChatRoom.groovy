package gpars.examples.actors.chat

import groovyx.gpars.actor.DefaultActor

/**
 * This example represents a chat room. It may have several members
 * sending random messages to it
 */
class ChatRoom extends DefaultActor {

    List<Member> members

    ChatRoom(List<Member> members) {
        this.members = members
        this.members*.chatRoom = this
    }

    void act() {
        loop {
            react { message ->
                println "[${sender.name}]: $message"
            }
        }
    }

    static void main(args) {

        def chatRoom =
            new ChatRoom(
                ['john', 'peter', 'francis'].collect { name ->
                    [name: name] as Member
                }
            )

        [chatRoom, *chatRoom.members].with { actors ->
            actors*.start()
            actors*.join()
        }

    }

}


