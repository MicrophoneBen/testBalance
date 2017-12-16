package server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public class DefaultRegistProvider implements RegistProvider {
    @Override
    public void regist(Object context) throws Exception {
        //创建临时节点，并写入基本信息
        //需要得到父节点路径
        //zkClient
        //serverData
        ZookeeperRegistContext registContext = (ZookeeperRegistContext)context;
        String path = registContext.getPath();
        ZkClient zkClient = registContext.getZkClient();
        try {
            zkClient.createEphemeral(path,registContext.getData());
        }catch (ZkNoNodeException e){//父节点不存在，先创建父节点
            zkClient.createEphemeral(path.substring(0,path.lastIndexOf("/")),true);
            regist(registContext);
        }
    }

    @Override
    public void unRegist(Object context) throws Exception {
        return;
    }
}
