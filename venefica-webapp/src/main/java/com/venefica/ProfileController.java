package com.venefica;

import com.venefica.auth.Token;
import com.venefica.auth.TokenDecryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final Log log = LogFactory.getLog(ProfileController.class);
    
    @Inject
    private UserDao userDao;
    
    @Inject
    TokenEncryptor tokenEncryptor;

    @RequestMapping(method = RequestMethod.GET)
    public void profile(@RequestParam("token") String encryptedToken, Model model) {
        try {
            Token token = tokenEncryptor.decrypt(encryptedToken);
            if (token != null) {
                User user = userDao.get(token.getUserId());

                if (user != null) {
                    model.addAttribute("token", encryptedToken);
                    model.addAttribute("fullName", user.getFullName());
                    model.addAttribute("email", user.getEmail());
                }
            }
        } catch (TokenDecryptionException e) {
            log.warn("Exception thrown when trying to decrypt token", e);
        }
    }
}
