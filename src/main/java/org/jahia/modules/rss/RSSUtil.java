/**
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *                                 http://www.jahia.com
 *
 *     Copyright (C) 2002-2018 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ==================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ===================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 */
package org.jahia.modules.rss;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import java.io.IOException;
import java.net.URL;


/**
 * Utility class for loading RSS feeds.
 * User: ktlili
 * Date: Jan 5, 2010
 * Time: 4:07:04 PM
 */
public class RSSUtil {

    private static final transient Logger logger = org.slf4j.LoggerFactory.getLogger(RSSUtil.class);
    
    private SyndFeed feed;
    
    private boolean loaded;

    private String url;

    /**
     * Get a SyndFeed from an url
     * @param url
     * @return
     */
    public static SyndFeed loadSyndFeed(String url){
        SyndFeed feed = null;
        try {
            final SyndFeedInput input = new SyndFeedInput();
            final XmlReader reader = new XmlReader(new URL(url));
            feed = input.build(reader);
        } catch (FeedException e) {
            logger.error("Error retrieving RSS feed from URL: " + url + ". Cause: " + e.getMessage(), e);
        } catch (IOException e) {
            // another way to load feed RSS
            final FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
            final FeedFetcher fetcher = new HttpURLFeedFetcher(feedInfoCache);
            try {
                feed = fetcher.retrieveFeed(new URL(url));
            } catch (Exception e1) {
                logger.error("Error retrieving RSS feed from URL: " + url + ". Cause: " + e1.getMessage(), e1);
            }
        } catch (Exception e) {
            logger.error("Error retrieving RSS feed from URL: " + url + ". Cause: " + e.getMessage(), e);
        }
        return feed;
    }

//    /**
//     * Reformat codes coming from google news
//     *
//     * @param entry
//     */
//    private static void reformatGoogleNewsEntry(SyndEntry entry) {
//        final SyndContent entryDescription = entry.getDescription();
//        if (entryDescription != null) {
//            final String description = entryDescription.getValue();
//            final int descBegin = description.lastIndexOf("<font size=-1>") + 14;
//            final int descEnd = description.indexOf("</font>", descBegin);
//
//            if (descBegin != -1 && descEnd != -1) {
//                final String realDesc = description.substring(descBegin, descEnd);
//                entryDescription.setValue(realDesc);
//            }
//        }
//    }
//    
    /**
     * Sets the feed URL to load and parse.
     * 
     * @param url the URl of the feed to load
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the parsed feed object. 
     * @return the parsed feed object
     */
    public SyndFeed getFeed() {
        if (!loaded) {
            loaded = true;
            feed = loadSyndFeed(url);
        }
        
        return feed;
    }
}