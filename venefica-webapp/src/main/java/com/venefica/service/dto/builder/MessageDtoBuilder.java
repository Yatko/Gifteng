package com.venefica.service.dto.builder;

import com.venefica.model.Image;
import com.venefica.model.Message;
import com.venefica.model.User;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.MessageDto;

public class MessageDtoBuilder extends DtoBuilderBase<Message, MessageDto> {

    private User currentUser;

    public MessageDtoBuilder(Message model) {
        super(model);
    }

    public MessageDtoBuilder setCurrentUser(User user) {
        currentUser = user;
        return this;
    }

    @Override
    public MessageDto build() {
        boolean owner = currentUser != null ? currentUser.equals(model.getFrom()) : false;
        Image toAvatar = model.getTo().getAvatar();
        Image fromAvatar = model.getFrom().getAvatar();
        
        MessageDto messageDto = new MessageDto();
        messageDto.setId(model.getId());
        messageDto.setAdId(model.getAd() != null ? model.getAd().getId() : null);
        messageDto.setText(model.getText());
        messageDto.setOwner(owner);
        messageDto.setToId(model.getTo().getId());
        messageDto.setToName(model.getTo().getName());
        messageDto.setToFullName(model.getTo().getFullName());
        messageDto.setToAvatarUrl(ImageDto.imageUrl(toAvatar));
        messageDto.setFromId(model.getFrom().getId());
        messageDto.setFromName(model.getFrom().getName());
        messageDto.setFromFullName(model.getFrom().getFullName());
        messageDto.setFromAvatarUrl(ImageDto.imageUrl(fromAvatar));
        messageDto.setRead(model.hasRead());
        messageDto.setCreatedAt(model.getCreatedAt());

        return messageDto;
    }
}
