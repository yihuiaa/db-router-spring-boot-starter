package cn.lyh.middleware.db.router.dynamic;

import cn.lyh.middleware.db.router.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ● @author: YiHui
 * ● @date: Created in 20:41  2023/8/27
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override protected Object determineCurrentLookupKey() {
        return "db"+ DBContextHolder.getDbKey();
    }
}
