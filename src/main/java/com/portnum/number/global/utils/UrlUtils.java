package com.portnum.number.global.utils;

import java.util.List;

public class UrlUtils {
    public static final List<String> EXCLUDE_URLS = List.of(
            "/auth/login", "/docs/index.html",
            "/admin/signup", "/admin/valid", "/admin/lost", "/admin/health",
            "/admin/image", "/auth/reissue", "/admin/popup/api"
    );

    public static final List<String> PERMITTED_URLS = List.of(
            "/admin/signup", "/admin/health", "/admin/popup/api/**",
            "/admin/valid/**", "/admin/lost/**", "/admin/image", "/auth/reissue"
    );
}

