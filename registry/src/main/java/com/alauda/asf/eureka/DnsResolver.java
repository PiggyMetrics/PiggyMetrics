package com.alauda.asf.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 *
 */
public class DnsResolver {
    private static final Logger logger = LoggerFactory.getLogger(DnsResolver.class);

    public List<String> resolveARecord(String domainName) {
        try {
            Record[] records = new Lookup(domainName, Type.A).run();

            return Arrays.stream(records).map( record -> record.rdataToString()).collect(Collectors.toList());
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot resole domain name " + domainName, e);
            }
            return Collections.emptyList();
        }
    }
}
