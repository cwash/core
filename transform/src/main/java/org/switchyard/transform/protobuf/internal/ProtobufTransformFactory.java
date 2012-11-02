package org.switchyard.transform.protobuf.internal;

import com.google.protobuf.Message;
import org.switchyard.common.xml.QNameUtil;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.TransformerFactory;
import org.switchyard.transform.config.model.ProtobufTransformModel;

import javax.xml.namespace.QName;

/**
 * Protobuf Transformer factory.
 *
 * @author Chris Wash <a href="mailto:chris.wash@gmail.com">chris.wash@gmail.com</a>
 */
public class ProtobufTransformFactory implements TransformerFactory<ProtobufTransformModel> {

    /**
     * Create a {@link Transformer} instance from the supplied {@link ProtobufTransformModel}.
     * @param model The Protobuf transformer config model.
     * @return the Transformer instance.
     */
    public Transformer newTransformer(ProtobufTransformModel model) {

        QName from = model.getFrom();
        QName to = model.getTo();

        assertValidProtobufTransformSpec(from, to);

        if (QNameUtil.isJavaMessageType(from)) {
            // Java to Protobuf
            return new Java2ProtobufTransformer();
        } else {
            // Protobuf to Java
            Class<Message> clazz = toJavaMessageType(to);
            return new Protobuf2JavaTransformer<Message>(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<Message> toJavaMessageType(QName name) {

        Class<Message> clazz = null;

        try {
            clazz = (Class<Message>) QNameUtil.toJavaMessageType(name);
        } catch (ClassCastException e) {
            throwClassDefinitionException(name);
        }

        if (clazz == null) {
            throwClassDefinitionException(name);
        }

        return clazz;
    }

    private void throwClassDefinitionException(QName name) {
        throw new SwitchYardException("Not able to find class definition "+ name);
    }

    private static void assertValidProtobufTransformSpec(QName from, QName to) {
    }
}
