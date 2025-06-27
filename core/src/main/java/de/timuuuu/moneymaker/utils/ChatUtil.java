package de.timuuuu.moneymaker.utils;

import java.util.regex.Pattern;

public class ChatUtil {

  public static final char COLOR_CHAR = '§';
  public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";

  public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile( "(?i)" + COLOR_CHAR + "[0-9A-FK-ORX]" );

  public static String stripColor(final String input) {
    if(input == null) return null;
    return STRIP_COLOR_PATTERN.matcher( input ).replaceAll( "" );
  }

}
