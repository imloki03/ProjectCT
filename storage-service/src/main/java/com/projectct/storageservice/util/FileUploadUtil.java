package com.projectct.storageservice.util;

import com.projectct.storageservice.constant.MessageKey;
import com.projectct.storageservice.exception.AppException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {
    public static final long MAX_FILE_SIZE = 2*1024*1024;
    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(final String filename, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(filename);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) {
        final long size = file.getSize();
        if (size > MAX_FILE_SIZE)
            throw new AppException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage(MessageKey.MEDIA_SIZE_LIMITED));
        final String fileName = file.getOriginalFilename();
        final String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        if (!isAllowedExtension(fileName, pattern))
            throw new AppException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, MessageUtil.getMessage(MessageKey.MEDIA_FILE_ALLOWED));

    }

    public static String getFileName(final String name) {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, name, date);
    }
}
