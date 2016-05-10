package com.adm.gaia.util;

import java.net.URI;

import com.adm.gaia.webhook.GaiaUrlContainer;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.transport.EtcdNettyClient;
import mousio.etcd4j.transport.EtcdNettyConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaiaEtcd {
    
    private static final Logger _logger = LoggerFactory.getLogger(GaiaEtcd.class);
    private static final String TOKEN_PATH = "gaia.itests/tokens/dexterlab";
    @Autowired
    private GaiaUrlContainer _urlContainer;
    private EtcdClient _etcdClient;
    
    private GaiaEtcd() {}

    public String getToken() {

        String ret = null;
        try {
            ret = client().get(TOKEN_PATH).send().get().node.value;
        } catch (EtcdException e) {
            if (!e.getMessage().toLowerCase().contains("key not found")) {
                throw new RuntimeException("Failed to read token from etcd", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read token from etcd", e);
        }

        return ret;
    }

    public void putToken(String token) {

        try {
            client().put(TOKEN_PATH, token).send().get();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to put token ID: %s in etcd (Path: %s)",token, TOKEN_PATH), e);
        }
    }

    private EtcdClient client() {
        
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