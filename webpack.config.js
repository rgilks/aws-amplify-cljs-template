const path = require('path')

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
    extensions: ['', '.js', '.jsx']
  }
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
    extensions: ['', '.js', '.jsx']
  }
}

module.exports = [appConfig, testConfig]
