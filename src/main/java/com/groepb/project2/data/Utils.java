package com.groepb.project2.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@42\\.nl$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}