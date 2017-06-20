package com.paperplanes.flagsquiz.database;

import com.paperplanes.flagsquiz.domain.Level;
import com.paperplanes.flagsquiz.domain.LevelStat;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by abdularis on 09/05/17.
 */

class SAXLevelHandler extends DefaultHandler {

    private ArrayList<Level> mLevels;

    @Override
    public void startDocument() throws SAXException {
        mLevels = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("level")) {
            Level level = new Level(attributes.getValue("name"),
                    attributes.getValue("file"),
                    Integer.parseInt(attributes.getValue("minToUnlock")),
                    new LevelStat());
            mLevels.add(level);
        }
    }

    ArrayList<Level> getLevels() {
        return mLevels;
    }
}
