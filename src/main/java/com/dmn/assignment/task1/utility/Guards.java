package com.dmn.assignment.task1.utility;

import com.dmn.assignment.task1.exception.CinemaException;
import org.apache.commons.lang3.StringUtils;

public class Guards {
    public static void mustPositive(int value, String errorMessage) {
        if (value <= 0)
            throw new CinemaException(errorMessage);
    }

    public static void notNullOrEmpty(String value, String errorMessage) {
        if (StringUtils.isBlank(value))
            throw new CinemaException(errorMessage);
    }
}
