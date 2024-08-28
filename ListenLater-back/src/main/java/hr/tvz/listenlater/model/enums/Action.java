package hr.tvz.listenlater.model.enums;

import lombok.Getter;

@Getter
public enum Action {
    LISTEN_LATER("LISTEN_LATER"),
    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    public static Action fromValue(String value) {
        for (Action action : Action.values()) {
            if (action.getValue().equals(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
