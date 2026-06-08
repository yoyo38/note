function chart(method) {
  let res = null;
  switch (method) {
    case "GET":
      res = [90, 48, 78, 10, 30, 95];
      break;
    default:
      res = null;
      break;
  }
  return res;
}

module.exports = chart;
