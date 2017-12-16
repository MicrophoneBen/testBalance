package client;

/**
 * Created by Administrator on 2017-12-15 0015.
 * 负载均衡器
 */
public interface BalanceProvider<T> {
    public T getBalanceItem();  //获取所有服务器负载的值
}
