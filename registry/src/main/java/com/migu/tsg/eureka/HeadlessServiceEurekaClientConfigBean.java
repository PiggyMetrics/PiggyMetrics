package com.migu.tsg.eureka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Profile;

/**
 * 
 * @author zhangq
 *
 */
@Profile("docker")
public class HeadlessServiceEurekaClientConfigBean extends EurekaClientConfigBean {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public static final String DEFAULT_SERVICE_NAME = "eureka";
    public static final String DEFAULT_SERVER_URL_CONTEXT = DEFAULT_SERVICE_NAME;
    
    private boolean useHeadlessService = true;
    // <zone, "headlessService1,headlessService2,...">
    private Map<String, String> headlessService = new HashMap<>();

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        if (!isUseHeadlessService()) {
            return super.getEurekaServerServiceUrls(myZone);
        }

        this.setUseDnsForFetchingServiceUrls(false);

        String hss = this.headlessService.get(myZone);
        if (hss == null || hss.isEmpty()) {
            hss = this.headlessService.get(DEFAULT_ZONE);
            if (hss == null || hss.isEmpty()) {
                hss = DEFAULT_SERVICE_NAME;
            }
        }

        final String[] hssSplit = hss.split(",");
        
        List<String> ips;
        List<String> urls = new ArrayList<>();
        String p = this.getEurekaServerPort();

        // Fall back to default value if EurekaServerURLContext is blank. 
        final String ctx = StringUtils.isBlank(getEurekaServerURLContext()) ? DEFAULT_SERVER_URL_CONTEXT : getEurekaServerURLContext();
        
        try {
            int pos;
            for (String s : hssSplit) {
                s = s.trim();
                pos = s.indexOf(":");
                
                DnsResolver dr = new DnsResolver();
                
                if (pos > 0) {
                    ips = dr.resolveARecord(s.substring(0, pos));
                    if (ips != null) {
                        String port = s.substring(pos + 1);
                        urls.addAll(ips.stream().map((e) -> "http://" + e + ":" + port + "/" + ctx + "/")
                                .collect(Collectors.toList()));
                    }
                }
                else {
                    ips = dr.resolveARecord(s);
                    if (ips != null) {
                        urls.addAll(ips.stream().map((e) -> "http://" + e + ":" + p + "/" + ctx + "/")
                                .collect(Collectors.toList()));
                    }
                }
            }
            
            if (urls.size() == 0) {
            		// fall back to default behavior
                urls.addAll(super.getEurekaServerServiceUrls(myZone));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return urls;
    }

    public boolean isUseHeadlessService() {
        return useHeadlessService;
    }

    public void setUseHeadlessService(boolean useHeadlessService) {
        this.useHeadlessService = useHeadlessService;
    }

    public Map<String, String> getHeadlessService() {
        return headlessService;
    }

    public void setHeadlessService(Map<String, String> headlessService) {
        this.headlessService = headlessService;
    }
}
