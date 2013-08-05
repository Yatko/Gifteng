package com.venefica;

import com.venefica.dao.ImageDao;
import com.venefica.model.Image;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Extract images from the database.
 *
 * @author Sviatoslav Grebenchukov
 */
@Controller
@RequestMapping("/images")
public class ImageController {

    @Inject
    private ImageDao imageDao;

    @RequestMapping("/img{imageId}")
    @Transactional
    public void image(
            @PathVariable Long imageId,
            HttpServletResponse response) throws IOException {
        Image img = imageDao.get(imageId);

        if (img == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        byte[] imageData = img.getData();

        response.setContentType(img.getImgType().getMimeType());
        response.setContentLength(imageData.length);

        ServletOutputStream out = response.getOutputStream();
        out.write(imageData);
        out.flush();
        out.close();
    }
}
