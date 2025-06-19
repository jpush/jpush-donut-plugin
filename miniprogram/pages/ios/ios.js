// pages/ios/ios.js
const { miniAppPluginId } = require('../../constant');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    myPlugin: undefined,
    // quickStartContents: [
    //   '在「设置」->「安全设置」中手动开启多端插件服务端口',
    //   '在「工具栏」->「运行设备」中选择 iOS 点击「运行」，快速准备运行环境',
    //   '在打开的 Xcode 中点击「播放」运行原生工程',
    //   '保持开发者工具开启，修改小程序代码和原生代码仅需在 Xcode 中点击「播放」查看效果',
    // ],
    logMessages: []
  },

  // 添加日志
  addLog(method, result) {
    const time = new Date().toLocaleTimeString()
    const log = {
      time,
      method,
      result: typeof result === 'object' ? JSON.stringify(result) : result
    }
    this.setData({
      logMessages: [log, ...this.data.logMessages].slice(0, 20)
    })
  },

  onLoadPlugin() {
    const listener = (param) => {
      this.addLog('事件处理', param)
    }
    wx.miniapp.loadNativePlugin({
      pluginId: miniAppPluginId,
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
  },

  onUsePlugin() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      console.log('plugin is undefined')
      return
    }
    const ret = myPlugin.mySyncFunc({ a: 'hello', b: [1,2] })
    console.log('mySyncFunc ret:', ret)

    myPlugin.myAsyncFuncwithCallback({ a: 'hello', b: [1,2] }, (ret) => {
      console.log('myAsyncFuncwithCallback ret:', ret)
    })

  },

  copyLink() {
    wx.setClipboardData({
      data: 'https://dev.weixin.qq.com/docs/framework/dev/plugin/iosPlugin.html',
    })
  },

  // JPush 基础方法
  setupJPush() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('初始化JPush', '插件未加载')
      return
    }
    myPlugin.setupJPush({
      appKey: '4fcc3e237eec4c4fb804ad49',
      channel: 'test',
      isProduction: false
    })
    this.addLog('初始化JPush', '已调用')
  },

  getRegistrationID() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('获取RegistrationID', '插件未加载')
      return
    }
    myPlugin.getRegistrationID({},(res) => {
      this.addLog('获取RegistrationID', res)
    })
  },

  setBadge() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('设置角标', '插件未加载')
      return
    }
    myPlugin.setBadge({
      badge: 1
    })
    this.addLog('设置角标', '已设置')
  },

  setLogLevel() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('设置日志级别', '插件未加载')
      return
    }
    myPlugin.setLogLevel({
      enable: true
    })
    this.addLog('设置日志级别', '已设置')
  },

  setSmartPushEnable() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('设置用户分群推送', '插件未加载')
      return
    }
    myPlugin.setSmartPushEnable({
      enable: true
    })
    this.addLog('设置用户分群推送', '已设置')
  },

  pageEnterTo() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('进入页面', '插件未加载')
      return
    }
    myPlugin.pageEnterTo({
      pageName: 'test_page'
    })
    this.addLog('进入页面', '已调用')
  },
  pageLeave() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('离开页面', '插件未加载')
      return
    }
    myPlugin.pageLeave({
      pageName: 'test_page'
    })
    this.addLog('离开页面', '已调用')
  },

  // 标签操作
  addTags() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('添加标签', '插件未加载')
      return
    }
    myPlugin.addTags({
      tags: ['tag1', 'tag2'],
      sequence: 1
    })
  },

  setTags() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('设置标签', '插件未加载')
      return
    }
    myPlugin.setTags({
      tags: ['tag1', 'tag2'],
      sequence: 2
    })
  },

  getAllTags() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('获取所有标签', '插件未加载')
      return
    }
    myPlugin.getAllTags({
      sequence: 3
    })
  },

  checkTagBindState() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('检查标签绑定状态', '插件未加载')
      return
    }
    myPlugin.checkTagBindState({
      tag: 'tag1',
      sequence: 4
    })
  },

  deleteTags() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('删除标签', '插件未加载')
      return
    }
    myPlugin.deleteTags({
      tags: ['tag1', 'tag2'],
      sequence: 5
    })
  },

  // 别名操作
  setAlias() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('设置别名', '插件未加载')
      return
    }
    myPlugin.setAlias({
      alias: 'test_alias',
      sequence: 6
    })
  },

  deleteAlias() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('删除别名', '插件未加载')
      return
    }
    myPlugin.deleteAlias({
      sequence: 7
    })
  },

  getAlias() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('获取别名', '插件未加载')
      return
    }
    myPlugin.getAlias({
      sequence: 8
    })
  },

  // 通知操作
  checkNotificationAuthorization() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('检查通知权限', '插件未加载')
      return
    }
    myPlugin.checkNotificationAuthorization({},(res) => {
      this.addLog('检查通知权限', res)
    })
  },

  openSettings() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      this.addLog('打开设置', '插件未加载')
      return
    }
    myPlugin.openSettings({},(res) => {
      this.addLog('打开设置', res)
    })
  }
})