config.resolve = {
    ...config.resolve,
    fallback: {
        "bufferutil": false,
        "crypto": false,
        "http": false,
        "https": false,
        "net": false,
        "stream": false,
        "tls": false,
        "url": false,
        "utf-8-validate": false,
        "zlib": false,
    },
};
