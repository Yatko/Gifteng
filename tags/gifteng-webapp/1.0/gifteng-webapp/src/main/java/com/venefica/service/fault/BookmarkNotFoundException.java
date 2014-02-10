package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The exception is thrown when the bookmark not found.
 *
 * @author Sviatoslav Grebenchukov
 */
@WebFault(name = "BookmarkNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class BookmarkNotFoundException extends Exception {

    public BookmarkNotFoundException(String message) {
        super(message);
    }

    public BookmarkNotFoundException(Long bookmarkId) {
        this("Bookmark not found for ad with id = " + bookmarkId + "!");
    }
}
