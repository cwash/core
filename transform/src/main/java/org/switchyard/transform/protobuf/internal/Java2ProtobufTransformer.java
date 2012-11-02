package org.switchyard.transform.protobuf.internal;

import org.switchyard.Message;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.BaseTransformer;

/**
 * CBW: write me
 */
public class Java2ProtobufTransformer extends BaseTransformer<Message, Message> {

    public Java2ProtobufTransformer() {
    }

    @Override
    public Message transform(Message message) {

        if (!(message.getContent() instanceof com.google.protobuf.Message)) {
             throw new SwitchYardException("Cannot marshall for type '" + getFrom() + "' because it does not appear to be a com.google.protobuf.Message.");
        }

        com.google.protobuf.Message protobufMessage = (com.google.protobuf.Message) message.getContent();
        byte[] protobufBytes = protobufMessage.toByteArray();
        message.setContent(protobufBytes);

        return message;
    }
}
