const Driver = require("./main");
const driver = new Driver.Driver(
    "root",
    "123456",
    "127.0.0.1",
    8888,
    false,
);

driver.listDatabase(
    console.log
);