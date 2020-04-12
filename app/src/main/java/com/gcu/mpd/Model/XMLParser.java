package com.gcu.mpd.Model;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class XMLParser {
    static public List<Region> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String description = null;
        String pubDate = null;
        String imgURL = null;

        boolean isImage =false;
        boolean isItem = false;

        List<Region> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            if (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                do {
                    int eventType = xmlPullParser.getEventType();

                    String name = xmlPullParser.getName();
                    if (name == null)
                        continue;

                    if (eventType == XmlPullParser.END_TAG) {
                        if (name.equalsIgnoreCase("image")) {
                            isImage = false;
                        }
                        if (name.equalsIgnoreCase("item")) {
                            isItem = false;
                        }
                        continue;
                    }

                    if (eventType == XmlPullParser.START_TAG) {
                        if (name.equalsIgnoreCase("image")) {
                            isImage = true;
                        }
                        if (name.equalsIgnoreCase("item")) {
                            isItem = true;
                            continue;
                        }
                    }

                    Log.d("MyXmlParser", "Parsing ==> " + name);
                    String result = "";
                    if (xmlPullParser.next() == XmlPullParser.TEXT) {
                        result = xmlPullParser.getText();
                        xmlPullParser.nextTag();
                    }

                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (isItem && name.equalsIgnoreCase("description")) {
                        description = result;
                    } else if (isItem && name.equalsIgnoreCase("pubDate")) {
                        pubDate = result;
                    } else if (isImage && name.equalsIgnoreCase("url")) {
                        imgURL = result;
                    }

                    if (title != null && pubDate != null && description != null && imgURL != null) {
                        Region Region = new Region(title, description, pubDate, imgURL);
                        items.add(Region);
                        title = null;
                        description = null;
                        pubDate = null;
                        isImage = false;
                        isItem = false;
                    }
                } while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT);
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
}
