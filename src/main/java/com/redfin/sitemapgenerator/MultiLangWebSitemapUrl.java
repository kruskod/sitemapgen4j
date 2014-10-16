package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Denis Krusko
 * @author e-mail: kruskod@gmail.com
 *         Created on 14/10/14.
 */
public class MultiLangWebSitemapUrl extends WebSitemapUrl {
    private Map<Locale,URL> alternatePages;

    /** Encapsulates a single simple URL */
    public MultiLangWebSitemapUrl(String url) throws MalformedURLException {
        super(url);
    }

    public MultiLangWebSitemapUrl(AbstractSitemapUrlOptions<?,?> options, Map<Locale,URL> alternatePages) {
        super(options);
        this.alternatePages = alternatePages;
    }

    /**
     * Configures the url with options
     */
    public MultiLangWebSitemapUrl(Options options) {
        super(options);
        alternatePages = options.alternatePages;
    }


    /** Options to configure web sitemap URLs */
    public static class Options extends AbstractSitemapUrlOptions<MultiLangWebSitemapUrl, Options>{
        private Map<Locale,URL> alternatePages;
        /** Configure this URL */
        public Options(String url)throws MalformedURLException {
            this(new URL(url));
        }

        /** Configure this URL */
        public Options(URL url) {
            super(url, MultiLangWebSitemapUrl.class);
        }

        public Options alternatePages(Map<Locale,URL> alternatePages) {
            this.alternatePages = alternatePages;
            return this;
        }
    }

    public Map<Locale, URL> getAlternatePages() {
        return alternatePages;
    }
}
