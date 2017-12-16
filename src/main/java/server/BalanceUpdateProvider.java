package server;

/**
 * Created by Administrator on 2017-12-15 0015.
 * 负载计数器接口
 */
public interface BalanceUpdateProvider {
    /**
     * 增加负载
     * @param step
     * @return
     */
    public boolean addBalance(Integer step);

    /**
     * 减少负载
     * @param step
     * @return
     */
    public boolean reduceBalance(Integer step);


}
