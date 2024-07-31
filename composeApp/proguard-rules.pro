# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class * implements com.harvest.common.interfaces.INoProguard
-keep class io.netty.** { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn org.apache.log4j.Level
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.log4j.Priority
-dontwarn org.apache.logging.log4j.Level
-dontwarn org.apache.logging.log4j.LogManager
-dontwarn org.apache.logging.log4j.Logger
-dontwarn org.apache.logging.log4j.message.MessageFactory
-dontwarn org.apache.logging.log4j.spi.ExtendedLogger
-dontwarn org.apache.logging.log4j.spi.ExtendedLoggerWrapper
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn reactor.blockhound.integration.BlockHoundIntegration

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.aayushatharva.brotli4j.Brotli4jLoader
-dontwarn com.aayushatharva.brotli4j.decoder.DecoderJNI$Status
-dontwarn com.aayushatharva.brotli4j.decoder.DecoderJNI$Wrapper
-dontwarn com.aayushatharva.brotli4j.encoder.BrotliEncoderChannel
-dontwarn com.aayushatharva.brotli4j.encoder.Encoder$Mode
-dontwarn com.aayushatharva.brotli4j.encoder.Encoder$Parameters
-dontwarn com.barchart.udt.OptionUDT
-dontwarn com.barchart.udt.SocketUDT
-dontwarn com.barchart.udt.StatusUDT
-dontwarn com.barchart.udt.TypeUDT
-dontwarn com.barchart.udt.nio.ChannelUDT
-dontwarn com.barchart.udt.nio.KindUDT
-dontwarn com.barchart.udt.nio.NioServerSocketUDT
-dontwarn com.barchart.udt.nio.NioSocketUDT
-dontwarn com.barchart.udt.nio.RendezvousChannelUDT
-dontwarn com.barchart.udt.nio.SelectorProviderUDT
-dontwarn com.barchart.udt.nio.ServerSocketChannelUDT
-dontwarn com.barchart.udt.nio.SocketChannelUDT
-dontwarn com.fasterxml.aalto.AsyncByteArrayFeeder
-dontwarn com.fasterxml.aalto.AsyncInputFeeder
-dontwarn com.fasterxml.aalto.AsyncXMLInputFactory
-dontwarn com.fasterxml.aalto.AsyncXMLStreamReader
-dontwarn com.fasterxml.aalto.stax.InputFactoryImpl
-dontwarn com.github.luben.zstd.Zstd
-dontwarn com.github.luben.zstd.ZstdInputStreamNoFinalizer
-dontwarn com.github.luben.zstd.util.Native
-dontwarn com.google.protobuf.ExtensionRegistry
-dontwarn com.google.protobuf.ExtensionRegistryLite
-dontwarn com.google.protobuf.MessageLite$Builder
-dontwarn com.google.protobuf.MessageLite
-dontwarn com.google.protobuf.MessageLiteOrBuilder
-dontwarn com.google.protobuf.Parser
-dontwarn com.google.protobuf.nano.CodedOutputByteBufferNano
-dontwarn com.google.protobuf.nano.MessageNano
-dontwarn com.jcraft.jzlib.Deflater
-dontwarn com.jcraft.jzlib.Inflater
-dontwarn com.jcraft.jzlib.JZlib$WrapperType
-dontwarn com.jcraft.jzlib.JZlib
-dontwarn com.ning.compress.BufferRecycler
-dontwarn com.ning.compress.lzf.ChunkDecoder
-dontwarn com.ning.compress.lzf.ChunkEncoder
-dontwarn com.ning.compress.lzf.LZFChunk
-dontwarn com.ning.compress.lzf.LZFEncoder
-dontwarn com.ning.compress.lzf.util.ChunkDecoderFactory
-dontwarn com.ning.compress.lzf.util.ChunkEncoderFactory
-dontwarn com.oracle.svm.core.annotate.Alias
-dontwarn com.oracle.svm.core.annotate.InjectAccessors
-dontwarn com.oracle.svm.core.annotate.RecomputeFieldValue$Kind
-dontwarn com.oracle.svm.core.annotate.RecomputeFieldValue
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn com.sun.nio.sctp.AbstractNotificationHandler
-dontwarn com.sun.nio.sctp.Association
-dontwarn com.sun.nio.sctp.AssociationChangeNotification
-dontwarn com.sun.nio.sctp.HandlerResult
-dontwarn com.sun.nio.sctp.MessageInfo
-dontwarn com.sun.nio.sctp.Notification
-dontwarn com.sun.nio.sctp.NotificationHandler
-dontwarn com.sun.nio.sctp.PeerAddressChangeNotification
-dontwarn com.sun.nio.sctp.SctpChannel
-dontwarn com.sun.nio.sctp.SctpServerChannel
-dontwarn com.sun.nio.sctp.SctpSocketOption
-dontwarn com.sun.nio.sctp.SctpStandardSocketOptions$InitMaxStreams
-dontwarn com.sun.nio.sctp.SctpStandardSocketOptions
-dontwarn com.sun.nio.sctp.SendFailedNotification
-dontwarn com.sun.nio.sctp.ShutdownNotification
-dontwarn gnu.io.CommPort
-dontwarn gnu.io.CommPortIdentifier
-dontwarn gnu.io.SerialPort
-dontwarn io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod
-dontwarn io.netty.internal.tcnative.AsyncTask
-dontwarn io.netty.internal.tcnative.Buffer
-dontwarn io.netty.internal.tcnative.CertificateCallback
-dontwarn io.netty.internal.tcnative.CertificateCompressionAlgo
-dontwarn io.netty.internal.tcnative.CertificateVerifier
-dontwarn io.netty.internal.tcnative.Library
-dontwarn io.netty.internal.tcnative.ResultCallback
-dontwarn io.netty.internal.tcnative.SSL
-dontwarn io.netty.internal.tcnative.SSLContext
-dontwarn io.netty.internal.tcnative.SSLPrivateKeyMethod
-dontwarn io.netty.internal.tcnative.SSLSession
-dontwarn io.netty.internal.tcnative.SSLSessionCache
-dontwarn io.netty.internal.tcnative.SessionTicketKey
-dontwarn io.netty.internal.tcnative.SniHostNameMatcher
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.DirContext
-dontwarn javax.naming.directory.InitialDirContext
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn lzma.sdk.ICodeProgress
-dontwarn lzma.sdk.lzma.Encoder
-dontwarn net.jpountz.lz4.LZ4Compressor
-dontwarn net.jpountz.lz4.LZ4Exception
-dontwarn net.jpountz.lz4.LZ4Factory
-dontwarn net.jpountz.lz4.LZ4FastDecompressor
-dontwarn net.jpountz.xxhash.XXHash32
-dontwarn net.jpountz.xxhash.XXHashFactory
-dontwarn org.bouncycastle.asn1.ASN1Encodable
-dontwarn org.bouncycastle.asn1.ASN1ObjectIdentifier
-dontwarn org.bouncycastle.asn1.ASN1OctetString
-dontwarn org.bouncycastle.asn1.ASN1Primitive
-dontwarn org.bouncycastle.asn1.DEROctetString
-dontwarn org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers
-dontwarn org.bouncycastle.asn1.pkcs.PrivateKeyInfo
-dontwarn org.bouncycastle.asn1.x500.X500Name
-dontwarn org.bouncycastle.asn1.x509.AccessDescription
-dontwarn org.bouncycastle.asn1.x509.AlgorithmIdentifier
-dontwarn org.bouncycastle.asn1.x509.AuthorityInformationAccess
-dontwarn org.bouncycastle.asn1.x509.Extension
-dontwarn org.bouncycastle.asn1.x509.Extensions
-dontwarn org.bouncycastle.asn1.x509.GeneralName
-dontwarn org.bouncycastle.asn1.x509.X509ObjectIdentifiers
-dontwarn org.bouncycastle.cert.X509CertificateHolder
-dontwarn org.bouncycastle.cert.X509v3CertificateBuilder
-dontwarn org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
-dontwarn org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
-dontwarn org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
-dontwarn org.bouncycastle.cert.ocsp.BasicOCSPResp
-dontwarn org.bouncycastle.cert.ocsp.CertificateID
-dontwarn org.bouncycastle.cert.ocsp.CertificateStatus
-dontwarn org.bouncycastle.cert.ocsp.OCSPException
-dontwarn org.bouncycastle.cert.ocsp.OCSPReq
-dontwarn org.bouncycastle.cert.ocsp.OCSPReqBuilder
-dontwarn org.bouncycastle.cert.ocsp.OCSPResp
-dontwarn org.bouncycastle.cert.ocsp.RevokedStatus
-dontwarn org.bouncycastle.cert.ocsp.SingleResp
-dontwarn org.bouncycastle.openssl.PEMDecryptorProvider
-dontwarn org.bouncycastle.openssl.PEMEncryptedKeyPair
-dontwarn org.bouncycastle.openssl.PEMKeyPair
-dontwarn org.bouncycastle.openssl.PEMParser
-dontwarn org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
-dontwarn org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder
-dontwarn org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder
-dontwarn org.bouncycastle.operator.ContentSigner
-dontwarn org.bouncycastle.operator.ContentVerifierProvider
-dontwarn org.bouncycastle.operator.DigestCalculator
-dontwarn org.bouncycastle.operator.DigestCalculatorProvider
-dontwarn org.bouncycastle.operator.InputDecryptorProvider
-dontwarn org.bouncycastle.operator.OperatorCreationException
-dontwarn org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
-dontwarn org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder
-dontwarn org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
-dontwarn org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo
-dontwarn org.conscrypt.AllocatedBuffer
-dontwarn org.conscrypt.BufferAllocator
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.HandshakeListener
-dontwarn org.eclipse.jetty.alpn.ALPN$ClientProvider
-dontwarn org.eclipse.jetty.alpn.ALPN$Provider
-dontwarn org.eclipse.jetty.alpn.ALPN$ServerProvider
-dontwarn org.eclipse.jetty.alpn.ALPN
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ClientProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$Provider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ServerProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego
-dontwarn org.jboss.marshalling.ByteInput
-dontwarn org.jboss.marshalling.ByteOutput
-dontwarn org.jboss.marshalling.Marshaller
-dontwarn org.jboss.marshalling.MarshallerFactory
-dontwarn org.jboss.marshalling.MarshallingConfiguration
-dontwarn org.jboss.marshalling.Unmarshaller
-dontwarn org.osgi.annotation.bundle.Export
-dontwarn reactor.blockhound.BlockHound$Builder
-dontwarn sun.security.x509.AlgorithmId
-dontwarn sun.security.x509.CertificateAlgorithmId
-dontwarn sun.security.x509.CertificateSerialNumber
-dontwarn sun.security.x509.CertificateSubjectName
-dontwarn sun.security.x509.CertificateValidity
-dontwarn sun.security.x509.CertificateVersion
-dontwarn sun.security.x509.CertificateX509Key
-dontwarn sun.security.x509.X500Name
-dontwarn sun.security.x509.X509CertImpl
-dontwarn sun.security.x509.X509CertInfo