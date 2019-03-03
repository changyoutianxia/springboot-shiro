package com.ch.springbootshiro.credentialsMatcher;


import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

public class CredentialsMatcherTest {
    @Test
    public void createPassword() {
        SimpleHash simpleHash = new SimpleHash("SHA-1", "123456", ByteSource.Util.bytes("1"), 2);
        System.out.println(simpleHash.toHex());

    }
}
