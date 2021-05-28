# hot-account
## 转账交易系统中的热点账户问题简单解决方案
尝试用java编写一个转账服务，传入交易流水号、转出账号，转入账号，转账金额，完成转出和转入账号的资金处理，该服务要确保在资金处理时转出账户的余额不会透支，金额计算准确，不需要考虑两个账户在不同的数据库，考虑两个人在不同的分表，两个人可能同时给对方转账。

进阶：当某一账户并发更新比较高时给数据库带来很大压力，如何应对这种热点账户问题。

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
