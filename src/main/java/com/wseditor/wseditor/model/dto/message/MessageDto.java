package com.wseditor.wseditor.model.dto.message;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wseditor.wseditor.model.dto.request.GetVersionRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartSessionMessage.class, name = "START_MESSAGE"),
        @JsonSubTypes.Type(value = GetUsersNamesMessage.class, name = "GET_USERS_NAME"),
        @JsonSubTypes.Type(value = UpdateTextMessageDto.class, name = "UPDATE_TEXT"),
        @JsonSubTypes.Type(value = NameUpdateMessage.class, name = "UPDATE_NAME"),
        @JsonSubTypes.Type(value = GetVersionRequest.class, name = "GET_VERSION"),
        @JsonSubTypes.Type(value = UpdateVersionMessage.class, name = "UPDATE_VERSION"),
        @JsonSubTypes.Type(value = DeleteVersionMessage.class, name = "DELETE_VERSION")
})

public abstract class MessageDto {

}
