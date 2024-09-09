package com.portnum.number.global.utils;

import java.util.List;

public class UrlUtils {
    public static final List<String> EXCLUDE_URLS = List.of(
            "/", "/h2", "/auth/login", "/docs/index.html",
            "/admin/signup", "/admin/valid", "/admin/lost", "/admin/health",
            "/admin/image", "/admin/reissue", "/admin/popup/api"
    );

    public static final List<String> PERMITTED_URLS = List.of(
            "/admin/signup", "/admin/health", "/admin/popup/api/**",
            "/admin/valid/**", "/admin/lost/**", "/admin/image", "/admin/reissue"
    );
}

