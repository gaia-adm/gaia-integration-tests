package com.adm.gaia.util;

import com.adm.gaia.common.GaiaITestException;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.transport.EtcdNettyClient;
import mousio.etcd4j.transport.EtcdNettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;

@Component
public class GaiaEtcd {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaEtcd.class);
    private static final String TOKEN_PATH = "gaia.itests/tokens/dexterlab";
    @Autowired
    private GaiaUrlContainer _urlContainer;
    private EtcdClient _etcdClient;

    private GaiaEtcd() {
    }

    public String getToken() {

        String ret = null;
        try {
            ret = client().get(TOKEN_PATH).send().get().node.value;
        } catch (EtcdException e) {
            if (!e.getMessage().toLowerCase().contains("key not found")) {
                throw new GaiaITestException("Failed to read token from etcd", e);
            }
        } catch (Exception e) {
            throw new GaiaITestException("Failed to read token from etcd", e);
        }

        return ret;
    }

    public void putToken(String token) {

        try {
            client().put(TOKEN_PATH, token).send().get();
        } catch (Exception e) {
            throw new GaiaITestException(String.format("Failed to put token ID: %s in etcd (Path: %s)",
                    token,
                    TOKEN_PATH), e);
        }
    }

    private EtcdClient client() {

        if (null == _etcdClient) {
            //Set Netty max frame size to 5MB instead of 100K to support 'select * ' stile operations, e.g. for EtcdTokenStore.getAccessToken method
            EtcdNettyConfig config = new EtcdNettyConfig();
            config.setMaxFrameSize(1024 * 100 * 50);
            _etcdClient =
                    new EtcdClient(new EtcdNettyClient(config, null, URI.create(getEtcdUrl())));
        }

        return _etcdClient;
    }

    private String getEtcdUrl() {

        String ret = _urlContainer.getGaiaEtcdUrl();
        if (ret == null || ret.isEmpty()) {
            try {
                _logger.debug("Effi *** InetAddress.getLocalHost(): " + InetAddress.getLocalHost());
                _logger.debug("Effi *** getHostAddress(): " + InetAddress.getLocalHost().getHostAddress());
                ret = String.format("http://%s:4001", InetAddress.getLocalHost().getHostAddress());
            } catch (Exception e) {
                throw new GaiaITestException("Failed to create etcd client using local host IP address",
                        e);
            }
        }
        _logger.debug("Etcd client URL: " + ret);

        return ret;
    }
}