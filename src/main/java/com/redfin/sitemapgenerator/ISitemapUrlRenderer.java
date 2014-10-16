package com.redfin.sitemapgenerator;

import java.io.IOException;
import java.io.Writer;

interface ISitemapUrlRenderer<T extends ISitemapUrl> {
	
	public Class<T> getUrlClass();
	public String getXmlNamespaces();
	public void render(T url, Writer out, W3CDateFormat dateFormat) throws IOException;
}
