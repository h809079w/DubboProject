项目名称：电商商城
两个子模块：
	商品秒杀系统
	基于spark的推荐系统
模块一：秒杀系统
	技术点：springboot，dubbo，zookeeper，rabbbitmq，redis，elasticsearch，nginx，mysql
		dubbo:服务拆分
		zookeeper:注册中心，分布式锁，防止重复秒杀
		rabbbitmq：秒杀异步下单，延时队列处理未支付订单
		redis:缓存
		elasticsearch：提供搜索功能
		nginx：实现前端跨域，静态页面缓存
模块二：推荐系统
	技术点：flume，kafka，spark，mysql
		flume：采集日志
		kafka：日志前处理
		spark：离线推荐和实时推荐存入mysql
重构：SpringCloud Alibaba重构，引入sentinel流量哨兵