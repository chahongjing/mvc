'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

var devEnv = require('./dev.env');
var prodEnv = require('./prod.env');
var context = devEnv.context;
var host = devEnv.baseHost;
var port = devEnv.basePort;
var targetHost = devEnv.targetHost;
var targetPort = devEnv.targetPort;
var prefix = devEnv.proxyPrefix;
if(context) context = context.replace(/'|"/g, "");
if(host) host = host.replace(/'|"/g, "");
if(port) port = port.replace(/'|"/g, "");
if(targetHost) targetHost = targetHost.replace(/'|"/g, "");
if(targetPort) targetPort = targetPort.replace(/'|"/g, "");
if(prefix) prefix = prefix.replace(/'|"/g, "");

var targetUrl = targetHost + (targetPort ? (':' + targetPort) : '');
var proxyTable = {};
var pathRewrite = {};
var buildPath = '../../web/src/main/resources/templates';
pathRewrite['^' + context + prefix] = context;
proxyTable[context + prefix] = {
    target: targetUrl,
    changeOrigin: true,  //是否跨域
    pathRewrite: pathRewrite
}

module.exports = {
  dev: {
    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    // proxyTable: {
    //     '/api': {
    //         // 测试环境
    //         target: 'http://localhost:8088/ToolSiteMvc4J',
    //         changeOrigin: true,  //是否跨域
    //         pathRewrite: {
    //             '^/api': ''   //需要rewrite重写的,
    //         }
    //     }
    // },
    proxyTable: proxyTable,

    // Various Dev Server settings
    host: host, // can be overwritten by process.env.HOST
    port: port || 8080, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    index: path.resolve(__dirname, buildPath + '/index.html'),

    // Paths 打包后文件存放的路径
    assetsRoot: path.resolve(__dirname, buildPath),
    // 除了index.html之外的静态资源要存放的路径
    assetsSubDirectory: '../static',
    // 代表打包后，index.html里面引用资源的相对地址
    assetsPublicPath: './',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
