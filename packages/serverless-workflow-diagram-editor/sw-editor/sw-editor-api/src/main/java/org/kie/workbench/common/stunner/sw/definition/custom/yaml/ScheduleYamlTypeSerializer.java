package org.kie.workbench.common.stunner.sw.definition.custom.yaml;

import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.YAMLDeserializer;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.YAMLSerializer;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.exception.YAMLDeserializationException;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.internal.deser.StringYAMLDeserializer;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.internal.deser.YAMLDeserializationContext;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.internal.ser.StringYAMLSerializer;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.internal.ser.YAMLSerializationContext;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.stream.YAMLSequenceWriter;
import org.kie.workbench.common.stunner.client.yaml.mapper.api.stream.YAMLWriter;
import org.kie.workbench.common.stunner.sw.definition.Schedule;
import org.kie.workbench.common.stunner.sw.definition.Schedule_YamlMapperImpl;


public class ScheduleYamlTypeSerializer implements YAMLDeserializer, YAMLSerializer {

    private static final Schedule_YamlMapperImpl mapper =
            Schedule_YamlMapperImpl.INSTANCE;


    private static final StringYAMLSerializer stringYAMLSerializer = new StringYAMLSerializer();
    private static final StringYAMLDeserializer stringYAMLDeserializer = new StringYAMLDeserializer();


    @Override
    public Object deserialize(YamlMapping yaml, String key, YAMLDeserializationContext ctx) throws YAMLDeserializationException {
        if (yaml == null || yaml.isEmpty() || yaml.value(key) == null) {
            return null;
        }
        YamlNode value = yaml.value(key);
        return deserialize(value, ctx);
    }

    @Override
    public Object deserialize(YamlNode node, YAMLDeserializationContext ctx) {
        if (node == null) {
            return null;
        }
        if(node.type() == com.amihaiemil.eoyaml.Node.SCALAR) {
            return stringYAMLDeserializer.deserialize(node, ctx);
        } else {
            return mapper.getDeserializer().deserialize(node, ctx);
        }
    }

    @Override
    public void serialize(YAMLWriter writer, String propertyName, Object value, YAMLSerializationContext ctx) {
        if (value instanceof String) {
            stringYAMLSerializer.serialize(writer, propertyName, (String) value, ctx);
        } else if (value instanceof Schedule) {
            mapper.getSerializer().serialize(writer, propertyName, (Schedule) value, ctx);
        }
    }

    @Override
    public void serialize(YAMLSequenceWriter writer, Object value, YAMLSerializationContext ctx) {
        if (value instanceof String) {
            stringYAMLSerializer.serialize(writer, (String) value, ctx);
        } else if (value instanceof Schedule) {
            mapper.getSerializer().serialize(writer, (Schedule) value, ctx);
        }
    }
}
