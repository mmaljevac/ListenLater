package hr.tvz.listenlater.model.enums;

import lombok.Getter;

@Getter
public enum InviteType {

    FRIEND_REQUEST("FRIEND_REQUEST"),
    ALBUM_RECOMMENDATION("ALBUM_RECOMMENDATION");

    private String value;

    private InviteType(String value) {
        this.value = value;
    }

    public static InviteType fromValue(String value) {
        for (InviteType inviteType : InviteType.values()) {
            if (inviteType.value.equals(value)) {
                return inviteType;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
