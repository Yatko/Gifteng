package com.venefica.service.dto.builder;

import com.venefica.model.Ad;
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
        boolean owner = (currentUser != null ? currentUser.matches(model.getFrom()) : false);
        Image toAvatar = model.getTo().getAvatar();
        Image fromAvatar = model.getFrom().getAvatar();
        Ad ad = model.getRequest() != null ? model.getRequest().getAd() : null;
        
        MessageDto messageDto = new MessageDto();
        messageDto.setId(model.getId());
        messageDto.setRequestId(model.getRequest() != null ? model.getRequest().getId() : null);
        messageDto.setAdId(ad != null ? ad.getId() : null);
        messageDto.setAdTitle(ad != null ? ad.getAdData().getTitle() : "");
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
        messageDto.setRead(model.isRead());
        messageDto.setCreatedAt(model.getCreatedAt());

        return messageDto;
    }
}
