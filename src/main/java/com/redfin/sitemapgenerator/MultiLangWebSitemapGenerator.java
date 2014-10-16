package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Denis Krusko
 * @author e-mail: kruskod@gmail.com
 *         Created on 14/10/14.
 */
public class MultiLangWebSitemapGenerator extends SitemapGenerator<MultiLangWebSitemapUrl, MultiLangWebSitemapGenerator> {
    private static final Pattern ESCAPE_HTML_PATTERN = Pattern.compile("(&)|(<)|(>)");
    private static final String[] ESCAPE_HTML_GROUPS = new String[]{"&amp;", "&lt;", "&gt;"};

    /**
     * Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<MultiLangWebSitemapGenerator> builder(URL baseUrl, File baseDir) {
        return new SitemapGeneratorBuilder<MultiLangWebSitemapGenerator>(baseUrl, baseDir, MultiLangWebSitemapGenerator.class);
    }

    /**
     * Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<MultiLangWebSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
        return new SitemapGeneratorBuilder<MultiLangWebSitemapGenerator>(baseUrl, baseDir, MultiLangWebSitemapGenerator.class);
    }

    MultiLangWebSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
        super(options, new Renderer());
    }

    /**
     * Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @throws java.net.MalformedURLException
     */
    public MultiLangWebSitemapGenerator(String baseUrl, File baseDir)
            throws MalformedURLException {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**
     * Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     */
    public MultiLangWebSitemapGenerator(URL baseUrl, File baseDir) {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**
     * Escape an html string. Escaping data received from the client helps to prevent cross-site script
     * vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }

        Matcher escapeMatcher = ESCAPE_HTML_PATTERN.matcher(html);
        if (!escapeMatcher.find()) {
            return html;
        } else {
            StringBuilder source = new StringBuilder(html);
            int matcherIndex = 0;
            while (escapeMatcher.find(matcherIndex)) {
                for (int i = escapeMatcher.groupCount(); i > 0; i--) {
                    if ((escapeMatcher.start(i)) != -1) {
                        source.replace(matcherIndex = escapeMatcher.start(i), escapeMatcher.end(i), ESCAPE_HTML_GROUPS[i]);
                        break;
                    }
                }
            }
            return source.toString();
        }
    }

    private static class Renderer extends AbstractSitemapUrlRenderer<MultiLangWebSitemapUrl> implements ISitemapUrlRenderer<MultiLangWebSitemapUrl> {

        public Class<MultiLangWebSitemapUrl> getUrlClass() {
            return MultiLangWebSitemapUrl.class;
        }

        public void render(MultiLangWebSitemapUrl url, Writer out,
                           W3CDateFormat dateFormat) throws IOException {
            StringBuilder sb = new StringBuilder();
            Locale urlLocale;
            if (url.getAlternatePages() != null) {

                for (Map.Entry<Locale, URL> item : url.getAlternatePages().entrySet()) {
                    urlLocale = item.getKey();
                    sb.append(String.format("    <xhtml:link rel=\"alternate\" hreflang=\"%s\" href=\"%s\" />\n", urlLocale.getLanguage(), item.getValue()));
                }
            }
            super.render(url, out, dateFormat, sb.toString());
        }

        public String getXmlNamespaces() {
            return "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"";
        }
    }
}
