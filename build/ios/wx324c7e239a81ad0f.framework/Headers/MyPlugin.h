//
//  myPlugin.h
//
//

#import <Foundation/Foundation.h>
#import "WeAppNativePlugin.framework/WeAppNativePlugin.h"
#import "JPUSHService.h"
#import <UserNotifications/UserNotifications.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyPlugin : WeAppNativePlugin <JPUSHRegisterDelegate, JPUSHInAppMessageDelegate, JPUSHNotiInMessageDelegate>

@end

NS_ASSUME_NONNULL_END




