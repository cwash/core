package org.switchyard.transform.config.model.v1;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.transform.TransformModel;
import org.switchyard.config.model.transform.v1.V1BaseTransformModel;
import org.switchyard.transform.TransformerFactoryClass;
import org.switchyard.transform.config.model.ProtobufTransformModel;
import org.switchyard.transform.protobuf.internal.ProtobufTransformFactory;

import javax.xml.namespace.QName;

/**
 * Version 1 Protobuf Transform Model.
 *
 * @author Chris Wash <a href="mailto:chris.wash@gmail.com">chris.wash@gmail.com</a>
 */
@TransformerFactoryClass(ProtobufTransformFactory.class)
public class V1ProtobufTransformModel extends V1BaseTransformModel implements ProtobufTransformModel {

    /**
     * Public constructor.
     */
    public V1ProtobufTransformModel() {
        super(new QName(TransformModel.DEFAULT_NAMESPACE, TransformModel.TRANSFORM + "." + PROTOBUF));
    }

    /**
     * Public constructor.
     * @param config configuration
     * @param desc description
     */
    public V1ProtobufTransformModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }
}
