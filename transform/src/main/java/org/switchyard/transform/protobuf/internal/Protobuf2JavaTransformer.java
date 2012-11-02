package org.switchyard.transform.protobuf.internal;

import com.google.protobuf.InvalidProtocolBufferException;
import org.switchyard.Message;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.BaseTransformer;

import java.lang.reflect.Method;

/**
 * CBW: write me
 */
public class Protobuf2JavaTransformer<T extends com.google.protobuf.Message> extends BaseTransformer<Message, Message> {

    private final T exampleInstance;

    public Protobuf2JavaTransformer(T exampleInstance) {
        this.exampleInstance = exampleInstance;
    }


    @SuppressWarnings({"unchecked", "NullArgumentToVariableArgMethod"})
    public Protobuf2JavaTransformer(Class<T> exampleClass) {
        try {
            Method defaultInstanceMethod = null;
            defaultInstanceMethod = exampleClass.getMethod("getDefaultInstance");
            this.exampleInstance = (T) defaultInstanceMethod.invoke(null, null);
        } catch (Exception e) {
            throw new SwitchYardException("Cannot setup Transformer for type '" + getFrom() + "' because it does not appear to be a com.google.protobuf.Message.", e);
        }
    }

    @Override
    public Message transform(Message message) {

        byte[] protobufBytes = (byte[]) message.getContent();
        com.google.protobuf.Message.Builder protobufBuilder = null;

        try {
            protobufBuilder = exampleInstance.newBuilderForType().mergeFrom(protobufBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new SwitchYardException("Cannot unmarshall for type '" + getFrom() + "'.", e);
        }

        if (!protobufBuilder.isInitialized()) {
            throw new SwitchYardException("Cannot unmarshall for type '" + getFrom() + "'.");
        }
        com.google.protobuf.Message protobufMessage = protobufBuilder.build();
        message.setContent(protobufMessage);

        return message;

    }
}
