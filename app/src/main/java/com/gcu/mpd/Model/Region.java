package com.gcu.mpd.Model;

/**
 * Mobile Platform Development
 * @author Iyosiyas Workie Mitiku - S1803446
 */
public class Region {
        public String title;
        public String description;
        public String pubDate;
        public String img;

        public Region(String title, String description, String pubDate, String imgURL) {
            this.title = title;
            this.description = description;
            this.pubDate = pubDate;
            this.img = imgURL;
        }

        @Override
        public String toString() {
            return "Region{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", pubDate='" + pubDate + '\'' +
                    '}';
        }
}

//    String locality, String date, String origin_time, String depth, String magnitude