package client;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import server.ServerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {
    private  String zkServer;//zookeeper服务器地址

    private  String serversPath; //zookeeper servers path

    private ZkClient zkClient;

    private static final Integer SESSION_TIME_OUT=5000;  //会话超时时间
    private static final Integer CONNECT_TIME_OUT=5000;  //连接超时时间

    public DefaultBalanceProvider(String zkServer,String serversPath){
       this.zkServer = zkServer;
       this.serversPath = serversPath;
       //创建zkClient
        zkClient = new ZkClient(zkServer,SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
    }

    /**
     * 负载均衡算法，获取所有items信息，将items按照从小到大排序，然后返回最小的item
     * @param items
     * @return
     */
    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
      if (items.size() > 0){
          Collections.sort(items);
          return items.get(0);
      }
        return null;
    }

    /**
     * 从zookeeper中拿到所有的工作服务器的列表
     * @return
     */
    @Override
    protected List<ServerData> getBalanceItems() {
        List<String> childList = zkClient.getChildren(this.serversPath);
        List<ServerData> serverDataList = new ArrayList<ServerData>();
        for (int i=0;i<childList.size();i++){
            ServerData sd = zkClient.readData(serversPath+"/"+childList.get(i));
            serverDataList.add(sd);
        }

        return serverDataList;
    }
}
