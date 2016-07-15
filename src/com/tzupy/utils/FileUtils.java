package com.tzupy.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides file helper functions.
 */
public class FileUtils {

    /**
     * Gets the formatted file size with trailing KB/MB/GB/TB
     * @param size the file size in bytes
     * @return the formatted file size
     */
    public static String getFileSizeFormatted(long size) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setRoundingMode(RoundingMode.FLOOR);
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(2);

        double kilobytes = 1024;
        double megabytes = kilobytes * kilobytes;
        double gigabytes = megabytes * kilobytes;
        double terabytes = gigabytes * kilobytes;

        if (size < kilobytes)
            return df.format(size) + " B";
        else if (size < megabytes)
            return df.format(size / kilobytes) + " KB";
        else if(size < gigabytes)
            return df.format(size / megabytes) + " MB";
        else if(size < terabytes)
            return df.format(size / gigabytes) + " GB";
        else
            return df.format(size / terabytes) + " TB";
    }

    public static String getFileLastModifiedFormatted(Date lastModified) {
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy, H:mm");
        return df.format(lastModified);
    }
}
