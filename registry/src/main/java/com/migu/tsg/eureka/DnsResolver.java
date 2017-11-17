package com.migu.tsg.eureka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zhangq
 *
 */
public class DnsResolver {
    private static final Logger logger = LoggerFactory.getLogger(DnsResolver.class);

    private static final String DNS_PROVIDER_URL = "dns:";
    private static final String DNS_CONTEXT_FACTORY = "com.sun.jndi.dns.DnsContextFactory";
    private static final String JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";
    private static final String JAVA_NAMING_PROVIDER_URL = "java.naming.provider.url";

    private static final String A_RECORD_TYPE = "A";
    // private static final String CNAME_RECORD_TYPE = "CNAME";
    // private static final String TXT_RECORD_TYPE = "TXT";

    @SuppressWarnings("unchecked")
	public List<String> resolveARecord(String domainName) {
        try {            
        		DirContext ctx = getDirContext();
        	
            Attributes attrs = ctx.getAttributes(domainName, new String[] { A_RECORD_TYPE });
            Attribute aRecords = attrs.get(A_RECORD_TYPE);
            NamingEnumeration<String> entries = (NamingEnumeration<String>) aRecords.getAll();
            List<String> result = new ArrayList<>(aRecords.size());

            while (entries.hasMore()) {
                result.add(entries.next());
            }

            logger.debug("resolveARecord returns {} for domainName={}", String.join(", ", result), domainName);
            
            return result;
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot resole domain name " + domainName, e);
            }
            return Collections.emptyList();
        }
    }

    public DirContext getDirContext() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(JAVA_NAMING_FACTORY_INITIAL, DNS_CONTEXT_FACTORY);
        env.put(JAVA_NAMING_PROVIDER_URL, DNS_PROVIDER_URL);

        logger.info("Initializing Dir Context...");
        
        try {
            return new InitialDirContext(env);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
