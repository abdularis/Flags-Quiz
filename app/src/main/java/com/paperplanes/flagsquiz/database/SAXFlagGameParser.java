package com.paperplanes.flagsquiz.database;

import com.paperplanes.flagsquiz.domain.FlagDetail;
import com.paperplanes.flagsquiz.domain.Level;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by abdularis on 04/05/17.
 */

class SAXFlagGameParser {

    static ArrayList<Level> getLevels(InputStream is) {
        ArrayList<Level> levels = null;

        try {
            SAXLevelHandler handler = new SAXLevelHandler();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(is));
            levels = handler.getLevels();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return levels;
    }

    static ArrayList<FlagDetail> getFlagDetails(InputStream is) {
        ArrayList<FlagDetail> flagDetails = null;
        try {
            SAXFlagDetailHandler handler = new SAXFlagDetailHandler();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(is));
            flagDetails = handler.getFlagDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flagDetails;
    }

    static int getFlagDetailsCount(InputStream is) {
        int count = 0;
        try {
            SAXFlagDetailCount handler = new SAXFlagDetailCount();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(is));
            count = handler.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

}
