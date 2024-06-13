//package com.transsion.authenticationfactory;
//
//import iaik.pkcs.pkcs11.Mechanism;
//import iaik.pkcs.pkcs11.objects.*;
//import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;
//import iaik.x509.V3Extension;
//import iaik.x509.extensions.BasicConstraints;
//import iaik.x509.extensions.CertificatePolicies;
//import iaik.x509.extensions.KeyUsage;
//import cn.hutool.core.codec.Base64Encoder;
//import iaik.asn1.ObjectID;
//import iaik.asn1.structures.AlgorithmID;
//import iaik.asn1.structures.PolicyInformation;
//import iaik.x509.X509Certificate;
//import iaik.x509.X509ExtensionException;
//
//import java.math.BigInteger;
//import java.security.Signature;
//import java.security.cert.CertificateEncodingException;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//import static com.transsion.authenticationfactory.infrastructure.utils.CertUtil.lf;
//
///**
// * @Description:
// * @Author jiakang.chen
// * @Date 2023/7/10
// */
//public class Authenticationfactory {
//
//
//    public static void main(String[] args) throws X509ExtensionException, CertificateEncodingException {
//        X509Certificate certificate = new iaik.x509.X509Certificate();
//
//        // set subject and issuer
//        //使用者
//        certificate.setSubjectDN(subjectName);
//        //颁发者
//        certificate.setIssuerDN(IssuerName);
//
//        // set public key
//        certificate.setPublicKey(publicKey);
//
//        // set serial number
//        //certificate.setSerialNumber(new BigInteger("1"));
//        certificate.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
//        // set validity
//        Calendar date = new GregorianCalendar();
//        date.add(Calendar.DATE, -5);
//        certificate.setValidNotBefore(date.getTime()); // valid from now
//        date.add(Calendar.YEAR, 3);
//        certificate.setValidNotAfter(date.getTime()); // for 3 years
//
//        // set extensions
//        V3Extension basicConstraints = new BasicConstraints(true);
//        certificate.addExtension(basicConstraints);
//
//        V3Extension keyUsage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment);
//        certificate.addExtension(keyUsage);
//
//        iaik.asn1.structures.PolicyQualifierInfo policyQualifierInfo = new iaik.asn1.structures.PolicyQualifierInfo(null, null,
//                "This certificate may be used for demonstration purposes only.");
//        PolicyInformation policyInformation = new PolicyInformation(new ObjectID("1.3.6.1.4.1.2706.2.2.1.1.1.1.1"),
//                new iaik.asn1.structures.PolicyQualifierInfo[]{policyQualifierInfo});
//        CertificatePolicies certificatePolicies = new CertificatePolicies(new PolicyInformation[]{policyInformation});
//        V3Extension policies = certificatePolicies;
//        certificate.addExtension(policies);
//
//        Signature tokenSignatureEngine = new PKCS11SignatureEngine("SHA256withRSA", session, Mechanism.get(PKCS11Constants.CKM_RSA_PKCS), AlgorithmID.sha256);
//        AlgorithmIDAdapter pkcs11Sha1RSASignatureAlgorithmID = new AlgorithmIDAdapter(AlgorithmID.sha256WithRSAEncryption);
//        pkcs11Sha1RSASignatureAlgorithmID.setSignatureInstance(tokenSignatureEngine);
//
//        java.security.PrivateKey tokenSignatureKey = new TokenPrivateKey(RSAPrivateKey);
//
//        certificate.sign(pkcs11Sha1RSASignatureAlgorithmID, tokenSignatureKey);
//
//        String crtStr = lf(Base64Encoder.encode(certificate.getEncoded()));
//    }
//}
