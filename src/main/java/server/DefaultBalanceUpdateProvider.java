package server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider {
    private String serverPath ;//zookeeper创建的临时节点的路径
    private ZkClient zkClient;

    public DefaultBalanceUpdateProvider(String serverPath,ZkClient zkClient){
        this.serverPath = serverPath;
        this.zkClient = zkClient;
    }

    /**
     * 先获取到当前的负载值，然后执行递增操作,然后回写到zookeeper中
     * @param step
     * @return
     */
    @Override
    public boolean addBalance(Integer step) {
        Stat stat = new Stat();
        ServerData serverData;
        while (true){
            try {
                serverData = zkClient.readData(serverPath,stat);
                serverData.setBalance(serverData.getBalance()+step);
                zkClient.writeData(serverPath,serverData,stat.getVersion());
                return  true;
            }catch (ZkBadVersionException e){
                //在赋值期间，如果已经被别的客户端修改，则会抛出此异常
                //所以引入循环，一直保存，直至成功
            }catch (Exception e){
                return false;
            }

        }

    }

    @Override
    public boolean reduceBalance(Integer step) {
        Stat stat = new Stat();
        ServerData serverData;
        while (true){
            try {
                serverData = zkClient.readData(serverPath,stat);
                serverData.setBalance(serverData.getBalance() >step ? serverData.getBalance()-step : 0);
                zkClient.writeData(serverPath,serverData,stat.getVersion());
                return  true;
            }catch (ZkBadVersionException e){
                //在赋值期间，如果已经被别的客户端修改，则会抛出此异常
                //所以引入循环，一直保存，直至成功
            }catch (Exception e){
                return false;
            }

        }
    }
}
