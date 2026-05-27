const path = require('path')
const webpack = require('webpack')
const CopyPlugin = require('copy-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const HtmlBeautifierPlugin = require('html-beautifier-webpack-plugin')

const appConfig = {
  mode: process.env.NODE_ENV || 'development',
  entry: './target/index.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'js/libs/bundle.js',
    clean: false
  },
  devtool: 'source-map',
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
        resolve: {
          fullySpecified: false,
          alias: {
            models: '../src/amplify/models/index.js',
            'aws-exports': '../src/amplify/aws-exports.js',
            js: '../src/js'
          }
        }
      }
    ]
  },
  resolve: {
    extensions: ['', '.js', '.jsx'],
    alias: {
      'js-cookie$': path.resolve(__dirname, 'src/js-cookie-compat.js'),
      process: 'process/browser'
    },
    fallback: {'process/browser': require.resolve('process/browser')}
  },
  plugins: [
    new webpack.ProvidePlugin({
      process: 'process/browser'
    }),
    new CopyPlugin({
      patterns: [
        {
          from: 'public',
          to: '.',
          globOptions: {
            ignore: ['**/index.html.tmpl']
          }
        }
      ]
    }),
    new HtmlWebpackPlugin({
      template: './public/index.html.tmpl',
      filename: 'index.html',
      templateParameters: {
        basePath: process.env.BASE_PATH ?? 'http://localhost:3000/'
      }
    }),
    new HtmlBeautifierPlugin()
  ]
}

const testConfig = {
  mode: 'development',
  entry: './karma/index.js',
  output: {
    path: path.resolve(__dirname, 'karma'),
    filename: 'js/libs/bundle.js',
    clean: false
  },
  devtool: 'source-map',
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
        resolve: {
          fullySpecified: false,
          alias: {
            models: '../src/amplify/models/index.js',
            'aws-exports': '../src/amplify/aws-exports.js',
            js: '../src/js'
          }
        }
      }
    ]
  },
  resolve: {
    extensions: ['', '.js', '.jsx'],
    alias: {
      'js-cookie$': path.resolve(__dirname, 'src/js-cookie-compat.js'),
      process: 'process/browser'
    },
    fallback: {'process/browser': require.resolve('process/browser')}
  }
}

module.exports = [appConfig, testConfig]
