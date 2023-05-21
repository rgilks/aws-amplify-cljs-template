const path = require('path')

const appConfig = {
  mode: 'development',
  entry: './target/index.js',
  output: {
    path: path.resolve(__dirname, 'public'),
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
    extensions: ['', '.js', '.jsx']
  }
}

const testConfig = {
  mode: 'development',
  entry: './browser-test/index.js',
  output: {
    path: path.resolve(__dirname, 'browser-test'),
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
    extensions: ['', '.js', '.jsx']
  }
}

module.exports = [appConfig, testConfig]
