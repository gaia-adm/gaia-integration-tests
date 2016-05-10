package com.adm.gaia.util;

import java.net.URI;

import com.adm.gaia.webhook.GaiaUrlContainer;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.transport.EtcdNettyClient;
import mousio.etcd4j.transport.EtcdNettyConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EtcdClientCreator {
    
    private static final Logger _logger = LoggerFactory.getLogger(EtcdClientCreator.class);
    @Autowired
    private GaiaUrlContainer _urlContainer;
    private static final EtcdClientCreator _instance = new EtcdClientCreator();
    private EtcdClient _etcdClient;
    
    private EtcdClientCreator() {}
    
    public static EtcdClientCreator getInstance() {
        
        return _instance;
    }
    
    public EtcdClient getEtcdClient() {
        
        if (null == _etcdClient) {
            //Set Netty max frame size to 5MB instead of 100K to support 'select * ' stile operations, e.g. for EtcdTokenStore.getAccessToken method
            EtcdNettyConfig config = new EtcdNettyConfig();
            config.setMaxFrameSize(1024 * 100 * 50);
            
            String etcdUrlString = System.getenv("etcdUrl");
            if (null == etcdUrlString) {
                URI[] defaultBaseUri = new URI[] { URI.create(_urlContainer.getGaiaEtcdUrl()) };
                _logger.debug("No Etcd URL provided, using the default: " + defaultBaseUri[0].toString());
                _etcdClient = new EtcdClient(new EtcdNettyClient(config, null, defaultBaseUri));
            } else {
                _logger.debug("Using Etcd URL " + etcdUrlString);
                _etcdClient =
                        new EtcdClient(
                                new EtcdNettyClient(config, null, URI.create(etcdUrlString)));
            }
        }
        
        return _etcdClient;
    }
}