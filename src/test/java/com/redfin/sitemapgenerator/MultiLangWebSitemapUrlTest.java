package com.redfin.sitemapgenerator;

import junit.framework.TestCase;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author Denis Krusko
 * @author e-mail: kruskod@gmail.com
 *         Created on 16/10/14.
 */
public class MultiLangWebSitemapUrlTest extends TestCase {
    File dir;
    MultiLangWebSitemapGenerator wsg;

    public void setUp() throws Exception {
        dir = File.createTempFile(MultiLangWebSitemapGenerator.class.getSimpleName(), "");
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();
    }

    public void tearDown() {
        wsg = null;
        for (File file : dir.listFiles()) {
            file.deleteOnExit();
            file.delete();
        }
        dir.delete();
        dir = null;
    }

    public void testSimpleUrl() throws Exception {
        wsg = new MultiLangWebSitemapGenerator("http://www.example.com", dir);
        Map<Locale, URL> alternatePages = new TreeMap<>(new Comparator<Locale>() {
            @Override
            public int compare(Locale o1, Locale o2) {
                if (o1 == null) return -1;
                if (o1.equals(o2)) {
                    return 0;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.toString().compareTo(o2.toString());
            }
        });
        alternatePages.put(Locale.GERMANY, new URL(SitemapGeneratorOptions.escapeHtml("http://www.example.com?locale=de&param=1")));
        alternatePages.put(Locale.ENGLISH, new URL(SitemapGeneratorOptions.escapeHtml("http://www.example.com?locale=en&param=<2")));
        alternatePages.put(Locale.FRANCE, new URL(SitemapGeneratorOptions.escapeHtml("http://www.example.com?locale=fr&param=>3")));

        MultiLangWebSitemapUrl url = new MultiLangWebSitemapUrl.Options("http://www.example.com")
                .changeFreq(ChangeFreq.HOURLY).lastMod(new Date(0)).priority(0.5)
                .alternatePages(alternatePages)
                .build();
        wsg.addUrl(url);
        wsg.addUrl("http://www.example.com/some_url");
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n" +
                "  <url>\n" +
                "    <loc>http://www.example.com</loc>\n" +
                "    <lastmod>1970-01-01T01:00+01:00</lastmod>\n" +
                "    <changefreq>hourly</changefreq>\n" +
                "    <priority>0.5</priority>\n" +
                "    <xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.example.com?locale=de&amp;param=1\" />\n" +
                "    <xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"http://www.example.com?locale=en&amp;param=&lt;2\" />\n" +
                "    <xhtml:link rel=\"alternate\" hreflang=\"fr\" href=\"http://www.example.com?locale=fr&amp;param=&gt;3\" />\n" +
                "  </url>\n" +
                "  <url>\n" +
                "    <loc>http://www.example.com/some_url</loc>\n" +
                "  </url>\n" +
                "</urlset>";
        String sitemap = writeMultiLangSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    private String writeMultiLangSiteMap(MultiLangWebSitemapGenerator wsg) {
        List<File> files = wsg.write();
        assertEquals("Too many files: " + files.toString(), 1, files.size());
        assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
        return TestUtil.slurpFileAndDelete(files.get(0));
    }
}
