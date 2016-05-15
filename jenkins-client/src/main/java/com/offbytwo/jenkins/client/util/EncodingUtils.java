package com.offbytwo.jenkins.client.util;

import com.google.common.net.UrlEscapers;

public final class EncodingUtils {

    private EncodingUtils() {
    } // nope

    public static String encode(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        String escape = UrlEscapers.urlPathSegmentEscaper().escape(pathPart);
        return escape;
    }

    public static String encodeParam(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        return UrlEscapers.urlFormParameterEscaper().escape(pathPart);
    }

}
