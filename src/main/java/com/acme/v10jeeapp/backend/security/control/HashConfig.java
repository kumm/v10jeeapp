package com.acme.v10jeeapp.backend.security.control;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HashConfig {
    private final static String HASH_ALGORITHM = Sha512Hash.ALGORITHM_NAME;
    private final static int HASH_ITERATIONS = 8;

    public Hash createHash(String password, ByteSource salt) {
        return new SimpleHash(HASH_ALGORITHM, password, salt, HASH_ITERATIONS);
    }

    public HashedCredentialsMatcher createMatcher() {
        HashedCredentialsMatcher credentialsMatcher
                = new HashedCredentialsMatcher(HASH_ALGORITHM);
        credentialsMatcher.setHashIterations(HASH_ITERATIONS);
        return credentialsMatcher;
    }
}
