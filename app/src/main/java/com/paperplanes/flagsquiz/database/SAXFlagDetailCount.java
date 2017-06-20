package com.paperplanes.flagsquiz.database;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by abdularis on 04/05/17.
 */

class SAXFlagDetailCount extends DefaultHandler {

    private int mCount;

    SAXFlagDetailCount() {
        mCount = 0;
    }

    int getCount() {
        return mCount;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("flag")) mCount++;
    }
}
