package com.paperplanes.flagsquiz.database;

import com.paperplanes.flagsquiz.domain.FlagDetail;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by abdularis on 04/05/17.
 */

class SAXFlagDetailHandler extends DefaultHandler {

    private ArrayList<FlagDetail> mFlagDetails;
    private FlagDetail mTempFlagDetail;
    private String mTempValue;

    SAXFlagDetailHandler() {
        mFlagDetails = new ArrayList<>();
    }

    ArrayList<FlagDetail> getFlagDetails() {
        return mFlagDetails;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        mTempValue = "";
        if (qName.equalsIgnoreCase("flag")) {
            mTempFlagDetail = new FlagDetail();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("flag")) {
            mFlagDetails.add(mTempFlagDetail);
        } else if (qName.equalsIgnoreCase("country")) {
            mTempFlagDetail.setCountry(mTempValue);
        } else if (qName.equalsIgnoreCase("capital")) {
            mTempFlagDetail.setCapital(mTempValue);
        } else if (qName.equalsIgnoreCase("continent")) {
            mTempFlagDetail.setContinent(mTempValue);
        } else if (qName.equalsIgnoreCase("image")) {
            mTempFlagDetail.setFlagAssetPath(mTempValue);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        mTempValue = new String(ch, start, length);
    }
}
