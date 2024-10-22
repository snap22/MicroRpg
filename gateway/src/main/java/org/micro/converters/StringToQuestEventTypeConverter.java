package org.micro.converters;

import org.micro.enums.QuestEventType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToQuestEventTypeConverter implements Converter<String, QuestEventType> {

    /**
     * Convert raw string to enum value
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return enum value
     */
    @Override
    public QuestEventType convert(@NonNull String source) {
        return QuestEventType.forValue(source);
    }
}
