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
		MessageDto messageDto = new MessageDto();
		messageDto.setId(model.getId());
		messageDto.setText(model.getText());

		boolean owner = currentUser != null ? currentUser.equals(model.getFrom()) : false;
		messageDto.setOwner(owner);

		Image toAvatar = model.getTo().getAvatar();
		Image fromAvatar = model.getFrom().getAvatar();
		messageDto.setToName(model.getTo().getName());
		messageDto.setToFullName(model.getTo().getFullName());
		messageDto.setToAvatarUrl(ImageDto.imageUrl(toAvatar));
		messageDto.setFromName(model.getFrom().getName());
		messageDto.setFromFullName(model.getFrom().getFullName());
		messageDto.setFromAvatarUrl(ImageDto.imageUrl(fromAvatar));
		messageDto.setRead(model.hasRead());
		messageDto.setCreatedAt(model.getCreatedAt());

		return messageDto;
	}
}
