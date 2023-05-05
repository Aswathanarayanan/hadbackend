package com.example.hadbackend.security.keys;

import com.example.hadbackend.security.Constants;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;

@RestController
public class KeysController {

    @GetMapping(value = "/keys/generate", produces = "application/json")
    public KeyMaterial generate() throws Exception {
        KeyPair keyPair = generateKeyPair();
        String receiverPrivateKey = getBase64String(getEncodedPrivateKey(keyPair.getPrivate()));
        String receiverPublicKey = getBase64String(getEncodedPublicKey(keyPair.getPublic()));
        String receiverNonce = generateRandomKey();
        return new KeyMaterial(receiverPrivateKey, receiverPublicKey, receiverNonce);
    }
    // generating key pair
    private KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constants.ALGORITHM, Constants.PROVIDER);
        X9ECParameters ecParameters = CustomNamedCurves.getByName(Constants.CURVE);
        ECParameterSpec ecSpec = new ECParameterSpec(ecParameters.getCurve(), ecParameters.getG(),
                ecParameters.getN(), ecParameters.getH(), ecParameters.getSeed());

        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    private String getBase64String(byte[] value) {
        return new String(org.bouncycastle.util.encoders.Base64.encode(value));
    }

    private byte[] getEncodedPrivateKey(PrivateKey key) throws Exception {
        ECPrivateKey ecKey = (ECPrivateKey) key;
        return ecKey.getD().toByteArray();
    }

    private byte[] getEncodedPublicKey(PublicKey key) throws Exception {
        ECPublicKey ecKey = (ECPublicKey) key;
        return ecKey.getQ().getEncoded(false);
    }
    // get random keys
    private String generateRandomKey() {
        byte[] salt = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return getBase64String(salt);
    }
}
