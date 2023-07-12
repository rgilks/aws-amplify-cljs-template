const path = require('path')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const HtmlBeautifierPlugin = require('html-beautifier-webpack-plugin')

const appConfig = {
  mode: 'development',
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
      process: 'process/browser'
    },
    fallback: {'process/browser': require.resolve('process/browser')}
  },
  plugins: [
    new webpack.ProvidePlugin({
      process: 'process/browser'
    }),
    new HtmlWebpackPlugin({
      template: './dist/index.html.tmpl',
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
      process: 'process/browser'
    },
    fallback: {'process/browser': require.resolve('process/browser')}
  }
}

module.exports = [appConfig, testConfig]
