package cn.lyh.middleware.db.router.strategy.impl;

import cn.lyh.middleware.db.router.DBContextHolder;
import cn.lyh.middleware.db.router.DBRouterConfig;
import cn.lyh.middleware.db.router.strategy.IDBRouterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ● @author: YiHui
 * ● @date: Created in 20:54  2023/8/27
 */
public class DBRouterStrategyHashCode implements IDBRouterStrategy {


    private Logger logger = LoggerFactory.getLogger(DBRouterStrategyHashCode.class);

    private DBRouterConfig dbRouterConfig;

    public DBRouterStrategyHashCode(DBRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
    }

    @Override public void doRouter(String dbKeyAttr) {
        logger.info("分库分表位置计算：dbKeyAttr:{}",dbKeyAttr);

        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        int idx = (size -1 ) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        //核心代码！
        int dbIdx  = idx/dbRouterConfig.getTbCount() + 1;
        //表位置等于idx - 当前库前面的表数量
        int tbIdx =  idx - dbRouterConfig.getTbCount()*(dbIdx-1);

        setDBKey(dbIdx);
        setTBKey(tbIdx);
        logger.info("分库分表位置计算：库序号:{}，表序号:{}",DBContextHolder.getDbKey(),DBContextHolder.getTbKey());


    }

    @Override public void setDBKey(int dbIdx) {
        DBContextHolder.setDbKey(String.format("%02d", dbIdx));
    }

    @Override public void setTBKey(int tbIdx) {
        DBContextHolder.setTbKey(String.format("%03d", tbIdx));
    }

    @Override public int dbCount() {
        return dbRouterConfig.getDbCount();
    }

    @Override public int tbCount() {
        return dbRouterConfig.getTbCount();
    }

    @Override public void clear() {
        DBContextHolder.clearDbKey();
        DBContextHolder.clearTbKey();
    }
}
