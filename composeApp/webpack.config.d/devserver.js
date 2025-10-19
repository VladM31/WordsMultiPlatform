// Enable development server features for better debugging
config.devServer = config.devServer || {};

// Open browser automatically
config.devServer.open = true;

// Enable hot reload
config.devServer.hot = true;

// Show overlay for errors and warnings
config.devServer.client = {
    overlay: {
        errors: true,
        warnings: true,
    },
    logging: 'info',
};

// Enable source maps for better debugging
config.devtool = 'eval-source-map';

// Log webpack info
console.log('Webpack DevServer is configured for debugging');

