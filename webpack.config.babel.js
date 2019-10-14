import path from 'path';
import EventEmitter from 'events';
import WebpackNotifierPlugin from 'webpack-notifier';
import RemovePlugin from 'remove-files-webpack-plugin';
import {ProvidePlugin} from 'webpack';
import CopyWebpackPlugin from 'copy-webpack-plugin';

import WatchEventsPlugin from './build-tooling/WatchEventsPlugin';

const merge = require('webpack-merge');
const tooling = require('./build-tooling/webpack.tooling');

const paths = {
  ROOT: __dirname,
  RELATIVE_ASSETS: 'build/resources/main/static/assets',
  ASSETS: path.join(__dirname, 'build/resources/main/static/assets'),
  ID7: path.join(__dirname, 'node_modules/@universityofwarwick/id7'),
  JS_ENTRY: {
    'main': './src/main/assets/js/main.js',
  },
  CSS_ENTRY: {
    'style': './src/main/assets/css/main.less',
  },
  CSS: './src/main/assets/css',
  IMAGES: './src/main/assets/images',
  PUBLIC_PATH: '/assets/',
};

const commonConfig = merge([
  {
    output: {
      path: paths.ASSETS,
      publicPath: paths.PUBLIC_PATH,
    },
    node: {
      // Fix Webpack global CSP violation https://github.com/webpack/webpack/issues/6461
      global: false,
    },
    plugins: [
      // Fix Webpack global CSP violation https://github.com/webpack/webpack/issues/6461
      new ProvidePlugin({
        global: require.resolve('./build-tooling/global.js'),
      }),
      new RemovePlugin({
        before: {
          root: paths.ROOT,
          include: [paths.RELATIVE_ASSETS],
        },
        after: {
          root: paths.ROOT,
          test: [
            {
              folder: paths.RELATIVE_ASSETS,
              method: filePath => (new RegExp(/style\.js.*$/, 'm').test(filePath)),
            },
          ],
        },
      }),
    ],
    resolve: {
      alias: {
        id7: paths.ID7,
      },
    },
    externals: {
      // Provided by ID7
      jquery: 'jQuery',
    },
  },
  tooling.lintJS(),
  tooling.transpileJS({
    entry: paths.JS_ENTRY,
    include: [
      /node_modules\/@universityofwarwick/,
      /src\/main\/assets\/js/,
    ]
  }),
  tooling.copyNpmDistAssets({
    dest: path.join(paths.ASSETS, 'lib'),
    modules: ['@universityofwarwick/id7'],
  }),
  {
    plugins: [
      new CopyWebpackPlugin([{
        from: 'node_modules/@fortawesome/fontawesome-pro/webfonts',
        to: path.join(paths.ASSETS, 'lib/fontawesome-pro/webfonts'),
      }]),
    ],
  },
  tooling.copyAssets({
    src: paths.IMAGES,
    dest: `${paths.ASSETS}/images`,
  }),
  tooling.extractCSS({
    entry: paths.CSS_ENTRY,
    resolverPaths: [
      paths.ID7,
    ],
  }),
]);

const productionConfig = merge([
  {
    mode: 'production',
  },
  tooling.transpileJS(),
  tooling.minify(),
  tooling.generateSourceMaps('source-map'),
]);

const developmentConfig = merge([
  {
    mode: 'development',
    plugins: [
      new WebpackNotifierPlugin(),
      new WatchEventsPlugin({emitter: new EventEmitter()}),
    ],
  },
  tooling.generateSourceMaps('cheap-module-source-map'),
]);

module.exports = ({production} = {}) => {
  if (production) {
    return merge(commonConfig, productionConfig);
  } else {
    return merge(commonConfig, developmentConfig);
  }
};
