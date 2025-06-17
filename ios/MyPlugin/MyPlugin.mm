#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "WeAppNativePlugin.framework/WeAppNativePlugin.h"
#import "MyPlugin.h"

__attribute__((constructor))
static void initPlugin() {
    [MyPlugin registerPluginAndInit:[[MyPlugin alloc] init]];
};

@interface MyPlugin ()

@end

@implementation MyPlugin

// 声明插件ID
WEAPP_DEFINE_PLUGIN_ID(wx324c7e239a81ad0f)

// 声明同步方法
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(setupJPush, @selector(setupJPush:))
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(setBadge, @selector(setBadge:))
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(setLogLevel, @selector(setLogLevel:))
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(setSmartPushEnable, @selector(setSmartPushEnable:))
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(pageEnterTo, @selector(pageEnterTo:))
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(pageLeave, @selector(pageLeave:))

// 声明异步方法
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(addTags, @selector(addTags:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(setTags, @selector(setTags:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(getAllTags, @selector(getAllTags:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(checkTagBindState, @selector(checkTagBindState:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(deleteTags, @selector(deleteTags:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(setAlias, @selector(setAlias:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(deleteAlias, @selector(deleteAlias:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(getAlias, @selector(getAlias:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(getRegistrationID, @selector(getRegistrationID:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(setPushSwitch, @selector(setPushSwitch:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(checkNotificationAuthorization, @selector(checkNotificationAuthorization:withCallback:))
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(openSettings, @selector(openSettings:withCallback:))

- (instancetype)init {
    self = [super init];
    if (self) {
    }
    return self;
}

// 插件初始化方法
- (void)initPlugin {
    NSLog(@"initPlugin");
    
    [self registerAppDelegateMethod:@selector(application:didFinishLaunchingWithOptions:)];
    [self registerAppDelegateMethod:@selector(application:didRegisterForRemoteNotificationsWithDeviceToken:)];
    
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    [JPUSHService registerDeviceToken:deviceToken];
}


// 同步方法实现
- (void)setupJPush:(NSDictionary *)param {
    
    dispatch_async(dispatch_get_main_queue(), ^{
        // 注册远程通知
        JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
        if (@available(iOS 12.0, *)) {
          entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound|JPAuthorizationOptionProvidesAppNotificationSettings;
          if (@available(iOS 13.0, *)) {
            entity.types = entity.types | JPAuthorizationOptionAnnouncement;
          }
        } else {
          entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound;
        }
        [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
        // 设置应用内消息代理
        [JPUSHService setInAppMessageDelegate:self];
        // 设置增强提醒代理
        [JPUSHService setNotiInMessageDelegate:self];
        // 添加自定义消息监听
        [[NSNotificationCenter defaultCenter] addObserver:self
                                               selector:@selector(networkDidReceiveMessage:)
                                                   name:kJPFNetworkDidReceiveMessageNotification
                                                 object:nil];
        
        NSString *appKey = param[@"appKey"];
        NSString *channel = param[@"channel"];
        BOOL isProduction = [param[@"isProduction"] boolValue];
        [JPUSHService setupWithOption:nil appKey:appKey channel:channel apsForProduction:isProduction];
    });
    
}

- (void)setBadge:(NSDictionary *)param {
    NSInteger badge = [param[@"badge"] integerValue];
    [JPUSHService setBadge:badge];
}

- (void)setLogLevel:(NSDictionary *)param {
    BOOL enable = [param[@"enable"] boolValue];
    if (enable) {
        [JPUSHService setDebugMode];
    } else {
        [JPUSHService setLogOFF];
    }
}

- (void)setSmartPushEnable:(NSDictionary *)param {
    BOOL enable = [param[@"enable"] boolValue];
    [JPUSHService setSmartPushEnable:enable];
}

- (void)pageEnterTo:(NSDictionary *)param {
    NSString *pageName = param[@"pageName"];
    [JPUSHService pageEnterTo:pageName];
}

- (void)pageLeave:(NSDictionary *)param {
    NSString *pageName = param[@"pageName"];
    [JPUSHService pageLeave:pageName];
}

// 异步方法实现
- (void)addTags:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSArray *tags = param[@"tags"];
    [JPUSHService addTags:[NSSet setWithArray:tags] completion:^(NSInteger iResCode, NSSet *iTags, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"message": @"success"});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)setTags:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSArray *tags = param[@"tags"];
    [JPUSHService setTags:[NSSet setWithArray:tags] completion:^(NSInteger iResCode, NSSet *iTags, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"message": @"success"});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)getAllTags:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    [JPUSHService getAllTags:^(NSInteger iResCode, NSSet *iTags, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"tags": [iTags allObjects]});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)checkTagBindState:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSString *tag = param[@"tag"];
    [JPUSHService validTag:tag completion:^(NSInteger iResCode, NSSet *iTags, NSInteger seq, BOOL isBind) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"isBind": @(isBind)});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)deleteTags:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSArray *tags = param[@"tags"];
    [JPUSHService deleteTags:[NSSet setWithArray:tags] completion:^(NSInteger iResCode, NSSet *iTags, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"message": @"success"});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)setAlias:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSString *alias = param[@"alias"];
    [JPUSHService setAlias:alias completion:^(NSInteger iResCode, NSString *iAlias, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"message": @"success"});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)deleteAlias:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    [JPUSHService deleteAlias:^(NSInteger iResCode, NSString *iAlias, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"message": @"success"});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)getAlias:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    [JPUSHService getAlias:^(NSInteger iResCode, NSString *iAlias, NSInteger seq) {
        if (iResCode == 0) {
            callback(@{@"code": @0, @"alias": iAlias ?: @""});
        } else {
            callback(@{@"code": @(iResCode), @"message": @"failed"});
        }
    } seq:0];
}

- (void)getRegistrationID:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
     [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString * _Nullable registrationID) {
        callback(@{@"registrationID": registrationID ?: @""});
    }];
    
}

- (void)setPushSwitch:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    BOOL enable = [param[@"enable"] boolValue];
    [JPUSHService setPushEnable:enable completion:^(NSInteger iResCode) {
        callback(@{@"code": @(iResCode)});
    }];
   
}

- (void)checkNotificationAuthorization:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
        BOOL granted = settings.authorizationStatus == UNAuthorizationStatusAuthorized;
        callback(@{@"code": @0, @"granted": @(granted)});
    }];
}

- (void)openSettings:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString] options:@{} completionHandler:^(BOOL success) {
            callback(@{@"code": @0, @"message": success ? @"success" : @"failed"});
        }];
    });
}


#pragma mark - JPUSHRegisterDelegate

- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler {
    NSDictionary *userInfo = notification.request.content.userInfo;
    [self sendMiniPluginEvent:@{@"onNotificationArrived": userInfo}];
    completionHandler(UNNotificationPresentationOptionBadge | UNNotificationPresentationOptionSound | UNNotificationPresentationOptionAlert);
}

- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
    NSDictionary *userInfo = response.notification.request.content.userInfo;
    [self sendMiniPluginEvent:@{@"onNotificationClicked": userInfo}];
    completionHandler();
}

#pragma mark - JPUSHInAppMessageDelegate

- (void)jPushInAppMessageDidShow:(JPushInAppMessage *)message {
    [self sendMiniPluginEvent:@{@"onInAppMessageShow": message.content}];
}

- (void)jPushInAppMessageDidClick:(JPushInAppMessage *)message {
    [self sendMiniPluginEvent:@{@"onInAppMessageClick": message.content}];
}

#pragma mark - 自定义消息处理

- (void)networkDidReceiveMessage:(NSNotification *)notification {
    NSDictionary *userInfo = notification.userInfo;
    [self sendMiniPluginEvent:@{@"onCustomMessage": userInfo}];
}

#pragma mark - JPUSHNotiInMessageDelegate
- (void)jPushNotiInMessageDidShowWithContent:(NSDictionary *)content {
    [self sendMiniPluginEvent:@{@"onNotiInMessageShow": content}];
}

- (void)jPushNotiInMessageDidClickWithContent:(NSDictionary *)content {
    [self sendMiniPluginEvent:@{@"onNotiInMessageClick": content}];
}

@end
