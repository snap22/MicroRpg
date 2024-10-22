package org.micro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestEventType {
    ACCEPTED("QuestAccepted"),
    PROGRESSED("QuestProgressed"),
    COMPLETED("QuestCompleted"),
    CANCELLED("QuestCancelled"),
    FAILED("QuestFailed");

    private final String eventTypeName;

    /**
     * Return enum value based on raw value
     * @param value raw value
     * @return quest event type
     */
    @JsonCreator
    public static QuestEventType forValue(String value) {
        for (QuestEventType type : QuestEventType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid type: " + value);
    }
}
