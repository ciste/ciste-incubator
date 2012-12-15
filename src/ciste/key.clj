(ns ciste.key
  (:import java.math.BigInteger
           java.security.KeyFactory
           java.security.KeyPair
           java.security.KeyPairGenerator
           java.security.PrivateKey
           java.security.KeyStore
           java.security.PublicKey
           java.security.Signature
           java.security.spec.RSAPrivateKeySpec
           java.security.spec.RSAPublicKeySpec
           org.apache.commons.codec.binary.Base64))

(def key-factory (KeyFactory/getInstance "RSA"))
(def keypair-generator (KeyPairGenerator/getInstance "RSA"))
(.initialize keypair-generator 1024)

(defn get-keystore
  ([] (get-keystore "JKS"))
  ([type] (KeyStore/getInstance type)))

(defn ^KeyPair generate-key
  "Generates a new RSA keypair"
  []
  (.genKeyPair keypair-generator))

(defn ^PublicKey public-key
  "Extracts the public key from the keypair"
  [^KeyPair keypair]
  (.getPublic keypair))

(defn ^PrivateKey private-key
  [^KeyPair keypair]
  (.getPrivate keypair))

(defn public-spec
  [^KeyPair keypair]
  (.getKeySpec key-factory (public-key keypair) RSAPublicKeySpec))

(defn private-spec
  [^KeyPair keypair]
  (.getKeySpec key-factory (private-key keypair) RSAPrivateKeySpec))


(defn ^String encode
  "Encode the byte array as a url-safe base-64 string"
  [^"[B" byte-array]
  (Base64/encodeBase64URLSafeString byte-array))

(defn ^"[B" decode
  "Decode the base64 string into a byte array."
  [^String data]
  (Base64/decodeBase64 data))

(defn ^BigInteger armored->big-int
  "converts an armored string to a BigInteger"
  [^String armored]
  (-> armored decode BigInteger.))

(defn ^"[B" get-bytes
  "Adapted from the java-salmon implementation"
  [^BigInteger bigint]
  (let [bitlen (.bitLength bigint)
        adjusted-bitlen (-> bitlen
                            (+ 7)
                            (bit-shift-right 3)
                            (bit-shift-left 3))]
    (if (< adjusted-bitlen bitlen)
      (throw (IllegalArgumentException. "Illegal bit len."))
      (let [bigbytes (.toByteArray bigint)
            biglen (alength bigbytes)
            bitmod  (mod bitlen 8)
            bitdiv (/ bitlen 8)]
        (if (and (not (zero? 0))
                 (= (inc bitdiv) (/ adjusted-bitlen 8)))
          bigbytes
          (let [start-src (if (zero? bitmod) 1 0)
                biglen2 (if (zero? bitmod) (dec biglen) biglen)
                start-dst (- (/ adjusted-bitlen 8) biglen2)
                new-size (/ adjusted-bitlen 8)
                resized-bytes (byte-array new-size)]
            (System/arraycopy
             bigbytes start-src resized-bytes
             start-dst biglen2)
            resized-bytes))))))


(defn get-base-string
  "Generate a signature base string"
  [^String armored-data
   ^String datatype
   ^String encoding
   ^String alg]
  (str armored-data "." datatype "." encoding "." alg))

;; (defn magic-key-string
;;   "Format keypair as a key string for use in webfinger."
;;   [^Key keypair]
;;   (when keypair
;;     (format "data:application/magic-public-key,RSA.%s.%s"
;;             (:n keypair) (:e keypair))))

(defn ^PublicKey get-key-from-armored
  [key-pair]
  (let [big-n (-> key-pair :n armored->big-int)
        big-e (-> key-pair :e armored->big-int)
        key-factory (KeyFactory/getInstance "RSA")
        key-spec (RSAPublicKeySpec. big-n big-e)]
    (.generatePublic key-factory key-spec)))

(defn sign
  "Signs the data with the private key and returns the result"
  [ ^"[B" data ^PrivateKey priv-key]
  (let [^Signature sig (Signature/getInstance "SHA256withRSA")]
    (doto sig
      (.initSign priv-key)
      (.update data))
    (.sign sig)))

(defn verify
  "Returns true if the signature is valid for the data and public key"
  [^"[B" data ^"[B" signature ^PublicKey key]
  ;; TODO: assert key is RSA
  (let [^Signature sig (Signature/getInstance "SHA256withRSA")]
    (doto sig
      (.initVerify key)
      (.update data))
    (.verify sig signature)))
