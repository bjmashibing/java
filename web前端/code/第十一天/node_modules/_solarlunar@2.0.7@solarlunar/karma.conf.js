'use strict';

var webpackCfg = require('./webpack.config.js');
var webpack = require('webpack');
var path = require('path');

module.exports = function (config) {
  var indexSpec = path.join(process.cwd(), './test/solarLunar.spec.js');
  var files = [
    indexSpec,
  ];
  var preprocessors = {};
  preprocessors[indexSpec] = ['webpack'];

  var reporters = ['mocha'];

  if (process.env.TRAVIS_JOB_ID) {
    reporters = ['coverage', 'coveralls'];
  }

  var coverageReporter = {
    reporters: [
      {
        type: 'lcov',
        subdir: '.',
      },
      {
        type: 'text',
      },
    ],
  };

  webpackCfg.module.rules.push({
    test: /\.jsx?$/,
    loader: 'istanbul-instrumenter-loader',
    include: [path.join(process.cwd(), './src')],
    enforce: 'post'
  });

  var conf = {
    reporters: reporters,
    singleRun: true,
    browsers: ['Electron'],
    electronOpts: {},
    client: {
      mocha: {
        reporter: 'html', // change Karma's debug.html to the mocha web reporter
        ui: 'bdd',
      },
    },
    frameworks: ['mocha'],
    files: files,
    preprocessors: preprocessors,
    coverageReporter: coverageReporter,
    webpack: webpackCfg,
    webpackServer: {
      noInfo: true, //please don't spam the console when running in karma!
    },
  };

  config.set(conf);
};
