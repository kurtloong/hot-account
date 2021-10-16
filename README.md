# hot-account
## 转账交易系统中的热点账户问题简单解决方案
尝试用java编写一个转账服务，传入交易流水号、转出账号，转入账号，转账金额，完成转出和转入账号的资金处理，该服务要确保在资金处理时转出账户的余额不会透支，金额计算准确，不需要考虑两个账户在不同的数据库，考虑两个人在不同的分表，两个人可能同时给对方转账。

进阶：当某一账户并发更新比较高时给数据库带来很大压力，如何应对这种热点账户问题。

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ed7acd9f639e4eafb746789be644308e)](https://app.codacy.com/gh/kurtloong/hot-account?utm_source=github.com&utm_medium=referral&utm_content=kurtloong/hot-account&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/cb5385f3291e4364967d1a03e7d58341)](https://www.codacy.com/gh/kurtloong/hot-account/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kurtloong/hot-account&amp;utm_campaign=Badge_Grade)

## 项目结构
```txt
├─entry-buffer
│  │  entry-buffer.iml
│  │  pom.xml
│  │  
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─com
│  │  │  │      └─github
│  │  │  │          └─kurtloong
│  │  │  │              └─entrybuffer
│  │  │  │                  │  EntryBufferApplication.java
│  │  │  │                  │  
│  │  │  │                  ├─config
│  │  │  │                  │      LocalDateTimeSerializerConfig.java
│  │  │  │                  │      
│  │  │  │                  ├─controller
│  │  │  │                  │      TradeController.java *测试Controller*
│  │  │  │                  │      
│  │  │  │                  ├─core
│  │  │  │                  │      EntryBufferTradeHandler.java *策略实现*
│  │  │  │                  │      TradeHandler.java 
│  │  │  │                  │      
│  │  │  │                  ├─domain
│  │  │  │                  │      Account.java *账户实体*
│  │  │  │                  │      Balance.java *钱包实体*
│  │  │  │                  │      Turnover.java *明细实体*
│  │  │  │                  │      TurnoverFlow.java *流水实体*
│  │  │  │                  │      
│  │  │  │                  ├─mapper
│  │  │  │                  │      AccountMapper.java
│  │  │  │                  │      BalanceMapper.java
│  │  │  │                  │      TurnoverFlowMapper.java
│  │  │  │                  │      TurnoverMapper.java
│  │  │  │                  │      
│  │  │  │                  ├─scheduled
│  │  │  │                  │      TurnoverFlowScheduled.java *定时把流水入账到明细*
│  │  │  │                  │      TurnoverFlowTask.java *分治*
│  │  │  │                  │      
│  │  │  │                  └─util
│  │  │  │                          RedisUtil.java
│  │  │  │                          TableUtil.java
│  │  │  │                          
│  │  │  └─resources
│  │  │      │  application.yml
│  │  │      │  
│  │  │      └─mapper
│  │  │              AccountMapper.xml
│  │  │              BalanceMapper.xml
│  │  │              TurnoverFlowMapper.xml
│  │  │              TurnoverMapper.xml
│  │  │              
│  │  └─test
│  │      └─java
│  │          └─test
│  │              └─com
│  │                  └─github
│  │                      └─kurtloong
│  │                          └─entrybuffer
│  │                              └─core
│  │                                      EntryBufferTradeHandlerTest.java
│  │                                      
│                                      
└─shadow-account *影子账户方法 暂未实现*
    │  pom.xml
    │  shadow-account.iml
    │  
    └─src
        ├─main
        │  ├─java
        │  └─resources
        └─test
            └─java



```

## 建表语句

```sql

create table demo.account1
(
	id int auto_increment
		primary key,
	account_id varchar(32) not null comment '账户',
	username varchar(16) not null comment '名称',
	create_time datetime null,
	update_time datetime null
);

create index idx_account_id
	on demo.account1 (account_id);


create table demo.account2
(
	id int auto_increment
		primary key,
	account_id varchar(32) not null comment '账户',
	username varchar(16) not null comment '名称',
	create_time datetime null,
	update_time datetime null
);

create index idx_account_id
	on demo.account2 (account_id);


create table demo.balance1
(
	id int auto_increment
		primary key,
	account_id varchar(32) null comment '账户',
	balance decimal(10,2) null comment '余额',
	create_time datetime null,
	update_time datetime null
);

create index idx_account_id
	on demo.balance1 (account_id);

create table demo.balance2
(
	id int auto_increment
		primary key,
	account_id varchar(32) null comment '账户',
	balance decimal(10,2) null comment '余额',
	create_time datetime null,
	update_time datetime null
);

create index idx_account_id
	on demo.balance2 (account_id);

create table demo.turnover
(
	id int auto_increment
		primary key,
	turnover_uid varchar(64) not null comment '上游产生的流水号',
	account_id varchar(32) not null comment '发生交易的账户',
	turnover_type tinyint(1) default 0 not null comment '0入账 1出账',
	turnover_version int null comment '版本号 -链式设计',
	before_balance decimal(10,2) not null comment '流水发生之前的余额',
	amount decimal(10,2) not null comment '发生交易的金额',
	after_balance decimal(10,2) not null comment '发生流水之后的金额',
	turnover_status tinyint(1) default 0 not null comment '回滚字段',
	update_time datetime null,
	create_time datetime null,
	constraint uk_turnover_uid_account_id
		unique (turnover_uid, account_id)
)
comment '流水明细表';

create table demo.turnover_flow
(
	id int auto_increment
		primary key,
	turnover_uid varchar(32) not null comment '流水uid',
	amount decimal(10,2) null comment '发生交易的金额',
	account_id varchar(32) not null comment '账户id',
	turnover_type int(1) not null comment '0-入账 1-出账',
	turnover_flow_status tinyint(1) default 0 null comment '是否入账 0-未入账 1-已入账',
	create_time datetime null,
	update_time datetime null,
	constraint uk_uid_id
		unique (turnover_uid, account_id)
)
comment '流水表';



```
