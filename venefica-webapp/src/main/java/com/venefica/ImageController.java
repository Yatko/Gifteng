package com.venefica;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.venefica.dao.ImageDao;
import com.venefica.model.Image;

/**
 * Extract images from the database.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Controller
@RequestMapping("/images")
public class ImageController {

	private static final String JPEG_MIME_TYPE = "image/jpeg";
	private static final String PNG_MIME_TYPE = "image/x-png";

	@Inject
	private ImageDao imageDao;

	@RequestMapping("/img{imageId}")
	@Transactional
	public void image(@PathVariable Long imageId, HttpServletResponse response) throws IOException {
		Image img = imageDao.get(imageId);

		if (img == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		switch (img.getImgType()) {
		case JPEG:
			response.setContentType(JPEG_MIME_TYPE);
			break;
		case PNG:
			response.setContentType(PNG_MIME_TYPE);
			break;
		default:
			response.setContentType(JPEG_MIME_TYPE);
		}

		byte[] imageData = img.getData();

		response.setContentLength(imageData.length);

		ServletOutputStream out = response.getOutputStream();
		out.write(imageData);
		out.flush();
		out.close();
	}
}
