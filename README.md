# Introduction

This repository is a simple attempt to use Redis for caching with
[micronaut](https://micronaut.io). However, it is failing reporting
that there is a duplicate cache.

# Replication Steps

To replicate this issue:

1. Start a redis server on your local machine (or, modify `application.yml`)
   with the URI for your redis server);
2. Start the app with `./gradlew run`
3. Attempt to hit the controller which will try and cache the results:
   `curl http://localhost:8080/testing/2`

At this point you'll see `curl` respond with:

```
$ curl http://localhost:8080/testing/200
{"message":"Internal Server Error: Error instantiating bean of type  [io.micronaut.cache.interceptor.CacheInterceptor]\n\nMessage: Cannot registry duplicate cache [io.micronaut.cache.DefaultSyncCache@39c71cb9] with cache manager. Ensure configured cache names are unique. Cache already configured for name [testing]: io.micronaut.configuration.lettuce.cache.RedisCache@6859ceac\nPath Taken: new $TestingControllerDefinition$Intercepted(BeanContext beanContext,[Interceptor[] interceptors]) --> new CacheInterceptor([CacheManager cacheManager],CacheErrorHandler errorHandler,AsyncCacheErrorHandler asyncCacheErrorHandler,ExecutorService ioExecutor,BeanContext beanContext)"}
```

And you'll see a stack trace of:

```
15:47:23.933 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 675ms. Server Running: http://localhost:8080
15:47:34.948 [pool-1-thread-3] INFO  io.lettuce.core.EpollProvider - Starting without optional epoll library
15:47:34.948 [pool-1-thread-3] INFO  io.lettuce.core.KqueueProvider - Starting without optional kqueue library
15:47:35.052 [pool-1-thread-3] ERROR i.m.h.s.netty.RoutingInBoundHandler - Unexpected error occurred: Error instantiating bean of type  [io.micronaut.cache.interceptor.CacheInterceptor]

Message: Cannot registry duplicate cache [io.micronaut.cache.DefaultSyncCache@39c71cb9] with cache manager. Ensure configured cache names are unique. Cache already configured for name [testing]: io.micronaut.configuration.lettuce.cache.RedisCache@6859ceac
Path Taken: new $TestingControllerDefinition$Intercepted(BeanContext beanContext,[Interceptor[] interceptors]) --> new CacheInterceptor([CacheManager cacheManager],CacheErrorHandler errorHandler,AsyncCacheErrorHandler asyncCacheErrorHandler,ExecutorService ioExecutor,BeanContext beanContext)
io.micronaut.context.exceptions.BeanInstantiationException: Error instantiating bean of type  [io.micronaut.cache.interceptor.CacheInterceptor]

Message: Cannot registry duplicate cache [io.micronaut.cache.DefaultSyncCache@39c71cb9] with cache manager. Ensure configured cache names are unique. Cache already configured for name [testing]: io.micronaut.configuration.lettuce.cache.RedisCache@6859ceac
Path Taken: new $TestingControllerDefinition$Intercepted(BeanContext beanContext,[Interceptor[] interceptors]) --> new CacheInterceptor([CacheManager cacheManager],CacheErrorHandler errorHandler,AsyncCacheErrorHandler asyncCacheErrorHandler,ExecutorService ioExecutor,BeanContext beanContext)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:1512)
	at io.micronaut.context.DefaultBeanContext.createAndRegisterSingleton(DefaultBeanContext.java:2174)
	at io.micronaut.context.DefaultBeanContext.getBeanForDefinition(DefaultBeanContext.java:1852)
	at io.micronaut.context.DefaultBeanContext.getBeanInternal(DefaultBeanContext.java:1832)
	at io.micronaut.context.DefaultBeanContext.getBean(DefaultBeanContext.java:997)
	at io.micronaut.context.AbstractBeanDefinition.getBeanForConstructorArgument(AbstractBeanDefinition.java:982)
	at io.micronaut.cache.interceptor.$CacheInterceptorDefinition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:1494)
	at io.micronaut.context.DefaultBeanContext.addCandidateToList(DefaultBeanContext.java:2507)
	at io.micronaut.context.DefaultBeanContext.getBeansOfTypeInternal(DefaultBeanContext.java:2407)
	at io.micronaut.context.DefaultBeanContext.getBeansOfType(DefaultBeanContext.java:854)
	at io.micronaut.context.AbstractBeanDefinition.lambda$getBeansOfTypeForConstructorArgument$10(AbstractBeanDefinition.java:1088)
	at io.micronaut.context.AbstractBeanDefinition.resolveBeanWithGenericsFromConstructorArgument(AbstractBeanDefinition.java:1697)
	at io.micronaut.context.AbstractBeanDefinition.getBeansOfTypeForConstructorArgument(AbstractBeanDefinition.java:1083)
	at io.micronaut.context.AbstractBeanDefinition.getBeanForConstructorArgument(AbstractBeanDefinition.java:959)
	at redis.test.$$TestingControllerDefinition$InterceptedDefinition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:1494)
	at io.micronaut.context.DefaultBeanContext.createAndRegisterSingleton(DefaultBeanContext.java:2174)
	at io.micronaut.context.DefaultBeanContext.getBeanForDefinition(DefaultBeanContext.java:1852)
	at io.micronaut.context.DefaultBeanContext.getBeanInternal(DefaultBeanContext.java:1832)
	at io.micronaut.context.DefaultBeanContext.getBean(DefaultBeanContext.java:577)
	at io.micronaut.context.DefaultBeanContext$BeanExecutionHandle.getTarget(DefaultBeanContext.java:2693)
	at io.micronaut.context.DefaultBeanContext$BeanExecutionHandle.invoke(DefaultBeanContext.java:2714)
	at io.micronaut.web.router.AbstractRouteMatch.execute(AbstractRouteMatch.java:295)
	at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:122)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.lambda$buildResultEmitter$17(RoutingInBoundHandler.java:1360)
	at io.reactivex.internal.operators.flowable.FlowableCreate.subscribeActual(FlowableCreate.java:71)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14752)
	at io.micronaut.reactive.rxjava2.RxInstrumentedFlowable.subscribeActual(RxInstrumentedFlowable.java:68)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.internal.operators.flowable.FlowableMap.subscribeActual(FlowableMap.java:37)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14752)
	at io.micronaut.reactive.rxjava2.RxInstrumentedFlowable.subscribeActual(RxInstrumentedFlowable.java:68)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty.subscribeActual(FlowableSwitchIfEmpty.java:32)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14752)
	at io.micronaut.reactive.rxjava2.RxInstrumentedFlowable.subscribeActual(RxInstrumentedFlowable.java:68)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14755)
	at io.micronaut.http.context.ServerRequestTracingPublisher.lambda$subscribe$0(ServerRequestTracingPublisher.java:52)
	at io.micronaut.http.context.ServerRequestContext.with(ServerRequestContext.java:52)
	at io.micronaut.http.context.ServerRequestTracingPublisher.subscribe(ServerRequestTracingPublisher.java:52)
	at io.reactivex.internal.operators.flowable.FlowableFromPublisher.subscribeActual(FlowableFromPublisher.java:29)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14752)
	at io.micronaut.reactive.rxjava2.RxInstrumentedFlowable.subscribeActual(RxInstrumentedFlowable.java:68)
	at io.reactivex.Flowable.subscribe(Flowable.java:14805)
	at io.reactivex.Flowable.subscribe(Flowable.java:14752)
	at io.reactivex.internal.operators.flowable.FlowableSubscribeOn$SubscribeOnSubscriber.run(FlowableSubscribeOn.java:82)
	at io.reactivex.internal.schedulers.ExecutorScheduler$ExecutorWorker$BooleanRunnable.run(ExecutorScheduler.java:288)
	at io.reactivex.internal.schedulers.ExecutorScheduler$ExecutorWorker.run(ExecutorScheduler.java:253)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: io.micronaut.context.exceptions.ConfigurationException: Cannot registry duplicate cache [io.micronaut.cache.DefaultSyncCache@39c71cb9] with cache manager. Ensure configured cache names are unique. Cache already configured for name [testing]: io.micronaut.configuration.lettuce.cache.RedisCache@6859ceac
	at io.micronaut.cache.DefaultCacheManager.<init>(DefaultCacheManager.java:53)
	at io.micronaut.cache.$DefaultCacheManagerDefinition.build(Unknown Source)
	at io.micronaut.context.DefaultBeanContext.doCreateBean(DefaultBeanContext.java:1494)
	... 56 common frames omitted
```