# 极光插件使用指南


## 一、集成

当前微信规定donut插件发布后不能给其他app使用。（直接使用插件id(wx324c7e239a81ad0f)集成，这种方式需要找微信工作人员加白名单使用。（不建议））

请根据下面步骤来集成：

1. 在多端应用中创建自己的donut插件。可以得到自己的donut插件id。
2. 将极光的插件代码移到自己的自己插件工程中, 需要特别注意插件id修改。
3. 构建插件产物,并上传。
4. 在多端工程中,应用插件。

### 1. iOS 配置插件

- 在微信开发者工具，前往`project.miniapp.json`，点击右上角切换到 json 模式，然后按照将下方内容添加`project.miniapp.json`

```json
"mini-plugin": {
  "ios": [
    {
      "open": true,
      "pluginId": "wx....",// 填自己创建的donut插件id
      "pluginVersion": "1.0.1" // 填自己创建的donut插件版本号
    }
  ]
```


### 2. Android 配置插件

#### 2.1 在微信开发者工具，前往`project.miniapp.json`，点击右上角切换到 json 模式，然后按照将下方内容添加`project.miniapp.json`

```json
"mini-plugin": {
  "android": [
    {
      "open": true,
      "pluginId": "wx....",// 填自己创建的donut插件id
      "pluginVersion": "1.0.1" // 填自己创建的donut插件版本号
    }
  ]
}
```

#### 2.2 配置manifestPlaceholders

- 在`mini-android`新增一个 key`manifestPlaceholders`，参考以下填写
- 请注意，插件目前集成了以下厂商：小米，oppo，vivo，荣耀，参数请参考极光的[厂商通道参数申请指南](https://docs.jiguang.cn/jpush/client/Android/android_3rd_param)
- 如果不需要厂商推送，则下面的对应厂商 key 保持默认的即可（请勿删除 key）
```json
  "mini-android": {
    "manifestPlaceholders": {
      "JPUSH_APPKEY": "你的 appkey",
      "JPUSH_CHANNEL": "developer-default"
    }
  }
```

## 二、加载插件

   调用 wx.miniapp.loadNativePlugin 方法加载插件。
   调用 onMiniPluginEvent 监听插件回调事件。

```javascript
    const listener = (param) => {
      this.addLog('事件处理', param)
    }
   wx.miniapp.loadNativePlugin({
      pluginId: 'wx324c7e239a81ad0f',
      success: (plugin) => {
        plugin.onMiniPluginEvent(listener)
      },
      fail: (e) => {
        console.log('load plugin fail', e)
      }
    })
```


## 三、API 参考文档


### setupJPush
初始化 JPush SDK

**方法签名：**
```javascript
setupJPush(data?: JSONObject): string
```

**参数：**
- `data` : 初始化参数对象
  - `appKey` (必需): 极光appkey
  - `channel` (必需): 操作序列号，用于跟踪操作
  - `isProduction` (必需): 是否生成环境

**返回值：**
无

**示例：**
```javascript
const result = myPlugin.setupJPush({
      appKey: '4fcc3e237eec4c4fb804ad49',
      channel: 'test',
      isProduction: false
    });
```

---

### addTags
添加标签

**方法签名：**
```javascript
addTags(data: JSONObject): void
```

**参数：**
- `data`: 标签参数对象
  - `tags` (必需): 标签数组，如 `["tag1", "tag2"]`
  - `sequence` (必需): 操作序列号，用于跟踪操作

**示例：**
```javascript
plugin.addTags({
    tags: ["vip", "android", "test"],
    sequence: 1
});
```

---

### setTags
设置标签（覆盖原有标签）

**方法签名：**
```javascript
setTags(data: JSONObject): void
```

**参数：**
- `data`: 标签参数对象
  - `tags` (必需): 标签数组，如 `["tag1", "tag2"]`
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.setTags({
    tags: ["new_tag1", "new_tag2"],
    sequence: 2
});
```

---

### getAllTags
获取所有标签

**方法签名：**
```javascript
getAllTags(data?: JSONObject): void
```

**参数：**
- `data` (必需): 参数对象
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.getAllTags({
    sequence: 3
});
```

---

### checkTagBindState
检查标签绑定状态

**方法签名：**
```javascript
checkTagBindState(data: JSONObject): void
```

**参数：**
- `data`: 参数对象
  - `tag` (必需): 要检查的标签名称
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.checkTagBindState({
    tag: "vip",
    sequence: 4
});
```

---

### deleteTags
删除标签

**方法签名：**
```javascript
deleteTags(data: JSONObject): void
```

**参数：**
- `data`: 参数对象
  - `tags` (必需): 要删除的标签数组
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.deleteTags({
    tags: ["old_tag1", "old_tag2"],
    sequence: 5
});
```

---

### setAlias
设置别名

**方法签名：**
```javascript
setAlias(data: JSONObject): void
```

**参数：**
- `data`: 参数对象
  - `alias` (必需): 别名字符串
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.setAlias({
    alias: "user_12345",
    sequence: 6
});
```

---

### deleteAlias
删除别名

**方法签名：**
```javascript
deleteAlias(data?: JSONObject): void
```

**参数：**
- `data` (必需): 参数对象
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.deleteAlias({
    sequence: 7
});
```

---

### getAlias
获取别名

**方法签名：**
```javascript
getAlias(data?: JSONObject): void
```

**参数：**
- `data` (必需): 参数对象
  - `sequence` (必需): 操作序列号

**示例：**
```javascript
plugin.getAlias({
    sequence: 8
});
```

---

### setBadge
设置角标数量

**方法签名：**
```javascript
setBadge(data: JSONObject): string
```

**参数：**
- `data`: 参数对象
  - `badge` (必需): 角标数量，整数

**返回值：**
无

**示例：**
```javascript
const result = plugin.setBadge({
    badge: 5
});
```

---

### setLogLevel
设置日志级别

**方法签名：**
```javascript
setLogLevel(data: JSONObject): string
```

**参数：**
- `data`: 参数对象
  - `enable` (必需): 是否启用调试日志，布尔值

**返回值：**
无

**示例：**
```javascript
const result = plugin.setLogLevel({
    enable: true
});
```

---

### setSmartPushEnable
设置智能推送开关

**方法签名：**
```javascript
setSmartPushEnable(data: JSONObject): string
```

**参数：**
- `data`: 参数对象
  - `enable` (必需): 是否启用智能推送，布尔值

**返回值：**
无

**示例：**
```javascript
const result = plugin.setSmartPushEnable({
    enable: true
});
```

---

### setCollectControl
设置数据收集控制

**方法签名：**
```javascript
setCollectControl(data: JSONObject): string
```

**参数：**
- `data`: 参数对象
  - `imei` (可选): 是否收集IMEI，布尔值
  - `imsi` (可选): 是否收集IMSI，布尔值
  - `mac` (可选): 是否收集MAC地址，布尔值
  - `ssid` (可选): 是否收集WiFi SSID，布尔值
  - `bssid` (可选): 是否收集WiFi BSSID，布尔值
  - `cell` (可选): 是否收集基站信息，布尔值
  - `wifi` (可选): 是否收集WiFi信息，布尔值
  - `gps` (可选): 是否收集地理位置信息，布尔值

**返回值：**
无

**示例：**
```javascript
// 开启完整数据收集（包含隐私参数）
const result1 = plugin.setCollectControl({
    imei: true,
    imsi: false,
    mac: false,
    ssid: true,
    bssid: false,
    cell: true,
    wifi: true,
    gps: true
});

### pageEnterTo
页面进入

**方法签名：**
```javascript
pageEnterTo(data?: JSONObject): string
```

**参数：**
- `data` (必需): 参数对象
    - `pageName` (必需): 页面名称

**返回值：**
无

**示例：**
```javascript
const result = plugin.pageEnterTo({
    pageName: 'page1'
});
```

---

### pageLeave
页面离开

**方法签名：**
```javascript
pageLeave(data?: JSONObject): string
```

**参数：**
- `data` (必需): 参数对象
    - `pageName` (必需): 页面名称

**返回值：**
无

**示例：**
```javascript
const result = plugin.pageLeave({
    pageName: 'page1'
});
```

### getRegistrationID
获取注册ID

**方法签名：**
```javascript
getRegistrationID(data: JSONObject, callback: Function): void
```

**参数：**
- `data`: 参数对象（可为空对象）
- `callback`: 回调函数

**回调参数：**
```json
{
    "code": 0,
    "registrationID": "1a2b3c4d5e6f7g8h9i0j"
}
```

**示例：**
```javascript
plugin.getRegistrationID({}, (result) => {
    if (result.code === 0) {
        console.log("注册ID:", result.registrationID);
    }
});
```

---

### setPushSwitch
设置推送开关

**方法签名：**
```javascript
setPushSwitch(data: JSONObject, callback: Function): void
```

**参数：**
- `data`: 参数对象
  - `enable` (必需): 是否启用推送，布尔值
- `callback`: 回调函数

**回调参数：**
```json
{
    "code": 0,
    "message": "success"
}
```

**示例：**
```javascript
plugin.setPushSwitch({
    enable: true
}, (result) => {
    if (result.code === 0) {
        console.log("启用推送成功");
    }
});
```

---

### checkNotificationAuthorization
检查通知权限

**方法签名：**
```javascript
checkNotificationAuthorization(data: JSONObject, callback: Function): void
```

**参数：**
- `data`: 参数对象（可为空对象）
- `callback`: 回调函数

**回调参数：**
```json
{
    "code": 0,
    "granted": true
}
```

**示例：**
```javascript
plugin.checkNotificationAuthorization({}, (result) => {
    if (result.code === 0) {
        if (result.granted) {
            console.log("通知权限已授予");
        } else {
            console.log("通知权限未授予");
        }
    }
});
```

---

### openSettings
打开系统设置

**方法签名：**
```javascript
openSettings(data: JSONObject, callback: Function): void
```

**参数：**
- `data`: 参数对象（可为空对象）
- `callback`: 回调函数

**回调参数：**
```json
{
    "code": 0,
    "message": "success"
}
```

**示例：**
```javascript
plugin.openSettings({}, (result) => {
    console.log("设置页面已打开");
});
```

## 事件监听

添加事件监听器 

在插件加载成功后，调用 onMiniPluginEvent 方法监听插件事件。

```javascript
    const listener = (param) => {
      this.addLog('事件处理', param)
    }
 wx.miniapp.loadNativePlugin({
      pluginId: 'wx324c7e239a81ad0f',
      success: (plugin) => {
        console.log('load plugin success', plugin)
        this.setData({
          myPlugin: plugin
        })
        this.addLog('加载插件', '成功')
        plugin.onMiniPluginEvent(listener)
      },
      fail: (e) => {
        console.log('load plugin fail', e)
        this.addLog('加载插件', e)
      }
    })
```

**回调的事件格式为：**
- `eventName`: 事件类型
- `eventData`: 事件具体内容 

** eventName 事件类型：**
      - "onConnectStatus":长连接状态回调,内容类型为boolean，true为连接
      - "onNotificationArrived":通知消息到达回调，内容为通知消息体
      - "onNotificationClicked":通知消息点击回调，内容为通知消息体
      - "onNotificationDeleted":通知消息删除回调，内容为通知消息体
      - "onCustomMessage":自定义消息回调，内容为通知消息体
      - "onPlatformToken":厂商token消息回调，内容为厂商token消息体 （仅Android）
      - "onTagMessage":tag操作回调
      - "onAliasMessage":alias操作回调
      - "onNotificationUnShow":在前台，通知消息不显示回调（后台下发的通知是前台信息时）
      - "onInAppMessageShow": 应用内消息展示
      - "onInAppMessageClick": 应用内消息点击
      - "onNotiInMessageShow": 增强提醒展示 （仅iOS）
      - "onNotiInMessageClick": 增强提醒点击 （仅iOS）

** eventData 事件回调数据：**

#### 推送通知事件
```json
{
    "eventName": "notification",
    "eventData": {
        "title": "推送标题",
        "content": "推送内容",
        "extras": {
            "key1": "value1",
            "key2": "value2"
        },
        "messageId": "msg_123456",
        "notificationId": 12345
    }
}
```

#### 自定义消息事件
```json
{
    "eventName": "message",
    "eventData": {
        "message": "自定义消息内容",
        "extras": {
            "key1": "value1"
        },
        "messageId": "msg_123456"
    }
}
```


#### 连接状态事件
```json
{
    "eventName": "connection",
    "event_data": {
        "connected": true,
        "reason": "连接成功"
    }
}
```
