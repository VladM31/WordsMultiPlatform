if (config.mode === 'development') {
  config.devServer = config.devServer || {};

  config.devServer.open = true;

  config.devServer.hot = true;

  config.devServer.client = {
      overlay: {
          errors: true,
          warnings: true,
      },
      logging: 'info',
  };

  config.devtool = 'eval-source-map';

  const defaultPort = 9000;
  config.devServer.port = defaultPort;

  console.log('Webpack DevServer is configured for debugging');
}
