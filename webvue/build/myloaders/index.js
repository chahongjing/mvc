let loaderUtils = require('loader-utils');

module.exports = function(source) {
  const options = loaderUtils.getOptions(this) || {};
  console.log('进入Loader了');
  // source = source.replace('#version#', new Date().getTime);
  console.log(source);
  return source;
}
