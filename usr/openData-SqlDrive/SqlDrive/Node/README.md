# Node Api
### 安装依赖 (npm, yarn, pnpm)
>**npm** 安装
>```shell
>npm install
>```

>**yarn** 安装
>```shell
>yarn install
>```

>**pnpm** 安装
>```shell
>pnpm install
>```

### 测试 (以**npm**为例)
```commandline
npm test
```
### 示例
*example.**ts***
```typescript
const Driver = require("./main");
const driver = new Driver.Driver(
    "root",         // username
    "123456",       // password
    "127.0.0.1",    // hostname
);

driver.listDatabase(
    console.log     // callback
);
```
##### 运行
```commandline
$ ts-node example.ts

[ 'linwin', 'wangguana', 'zmh-program' ]
```