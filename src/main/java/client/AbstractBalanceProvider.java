package client;

import java.util.List;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public abstract class AbstractBalanceProvider<T> implements BalanceProvider<T>{
    //负载均衡算法，从一系列items中找出最合适的服务器
    protected abstract T balanceAlgorithm(List<T> items);

    protected abstract List<T> getBalanceItems(); //获取所有的负载均衡列表


    @Override
    public T getBalanceItem() {
        return balanceAlgorithm(getBalanceItems());
    }
}
