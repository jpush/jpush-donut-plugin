#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "WeAppNativePlugin.framework/WeAppNativePlugin.h"
#import "MyPlugin.h"

__attribute__((constructor))
static void initPlugin() {
    [MyPlugin registerPluginAndInit:[[MyPlugin alloc] init]];
};

@implementation MyPlugin

// 声明插件ID
WEAPP_DEFINE_PLUGIN_ID(wx324c7e239a81ad0f)

// 声明插件同步方法
WEAPP_EXPORT_PLUGIN_METHOD_SYNC(mySyncFunc, @selector(mySyncFunc:))

// 声明插件异步方法
WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(myAsyncFuncwithCallback, @selector(myAsyncFunc:withCallback:))

- (id)mySyncFunc:(NSDictionary *)param {
    NSLog(@"mySyncFunc %@", param);
        
    return @"mySyncFunc";
}

- (void)myAsyncFunc:(NSDictionary *)param withCallback:(WeAppNativePluginCallback)callback {
    NSLog(@"myAsyncFunc %@", param);
    callback(@{ @"a": @"1", @"b": @[@1, @2], @"c": @3 });
}

// 插件初始化方法，在注册插件后会被自动调用
- (void)initPlugin {
    NSLog(@"initPlugin");
    [self registerAppDelegateMethod:@selector(application:openURL:options:)];
    [self registerAppDelegateMethod:@selector(application:continueUserActivity:restorationHandler:)];
}

- (void)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options {
    NSLog(@"url scheme");
}

- (void)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> *__nullable restorableObjects))restorationHandler {
    NSLog(@"universal link");
}

@end
