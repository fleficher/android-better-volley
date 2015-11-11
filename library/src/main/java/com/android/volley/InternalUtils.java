package com.android.volley;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: mcxiaoke
 * Date: 15/3/17
 * Time: 14:47
 */
public class InternalUtils {

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     */
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
     */
    public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in ANSI C
     * {@code asctime()} format.
     */
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

	   private static final String[] DEFAULT_PATTERNS = new String[] {
		              PATTERN_RFC1123,
		              PATTERN_RFC1036,
		              PATTERN_ASCTIME
		           };
    
	public static String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss",
				Locale.US);
		return df.format(date);
	}
	
    public static Date parseDate(final String dateValue) {
        final String[] localDateFormats = DEFAULT_PATTERNS;
        final Date localStartDate = new Date();
        String v = dateValue;
        if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
            v = v.substring (1, v.length() - 1);
        }

        for (final String dateFormat : localDateFormats) {
            final SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            dateParser.set2DigitYearStart(localStartDate);
            final ParsePosition pos = new ParsePosition(0);
            final Date result = dateParser.parse(v, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }
        return null;
    }
  
  final static class DateFormatHolder {
  	  
      private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>
          THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {

          @Override
          protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
              return new SoftReference<Map<String, SimpleDateFormat>>(
                      new HashMap<String, SimpleDateFormat>());
          }

      };

      public static SimpleDateFormat formatFor(final String pattern) {
          final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
          Map<String, SimpleDateFormat> formats = ref.get();
          if (formats == null) {
              formats = new HashMap<String, SimpleDateFormat>();
              THREADLOCAL_FORMATS.set(
                      new SoftReference<Map<String, SimpleDateFormat>>(formats));
          }

          SimpleDateFormat format = formats.get(pattern);
          if (format == null) {
              format = new SimpleDateFormat(pattern, Locale.US);
              format.setTimeZone(TimeZone.getTimeZone("GMT"));
              formats.put(pattern, format);
          }

          return format;
      }

      public static void clearThreadLocal() {
          THREADLOCAL_FORMATS.remove();
      }

  }
	
}
