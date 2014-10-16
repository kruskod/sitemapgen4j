package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SitemapGeneratorOptions extends
		AbstractSitemapGeneratorOptions<SitemapGeneratorOptions> {
    private static final Pattern ESCAPE_HTML_PATTERN = Pattern.compile("(&)|(<)|(>)");
    private static final String[] ESCAPE_HTML_GROUPS = new String[]{"&amp;", "&lt;", "&gt;"};

	public SitemapGeneratorOptions(URL baseUrl, File baseDir) {
		super(baseUrl, baseDir);
	}
	
	public SitemapGeneratorOptions(String baseUrl, File baseDir) throws MalformedURLException {
		this(new URL(escapeHtml(baseUrl)), baseDir);
	}

    public static String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        StringBuilder source = new StringBuilder(html);
        Matcher escapeMatcher = ESCAPE_HTML_PATTERN.matcher(source);
        if (!escapeMatcher.find()) {
            return html;
        } else {
            int matcherIndex = 0;
            while (escapeMatcher.find(matcherIndex)) {
                for (int i = escapeMatcher.groupCount(); i > 0; i--) {
                    if ((escapeMatcher.start(i)) != -1) {
                        matcherIndex = escapeMatcher.end(i);
                        source.replace(escapeMatcher.start(i), matcherIndex, ESCAPE_HTML_GROUPS[i - 1]);
                        break;
                    }
                }
            }
            return source.toString();
        }
    }

}
