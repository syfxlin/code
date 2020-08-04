<?php

namespace Utils;

class Crypt
{
    /**
     * 用于加密的 Key
     *
     * @var string
     */
    protected $key;

    /**
     * 加密算法
     *
     * @var string
     */
    protected $cipher;

    /**
     * 加密构造器
     *
     * @param   string  $key     .env APP_KEY
     * @param   string  $cipher  .env APP_CIPHER
     *
     * @return  this
     */
    public function __construct(string $key, string $cipher = 'AES-256-CBC')
    {
        if ($this->supported($key, $cipher)) {
            $this->key = $key;
            $this->cipher = $cipher;
        } else {
            throw new \RuntimeException('The only supported ciphers are AES-128-CBC and AES-256-CBC with the correct key lengths.');
        }
    }

    /**
     * 验证加密算法和 Key
     *
     * @param   string  $key     Key
     * @param   string  $cipher  加密算法
     *
     * @return  bool             是否合法
     */
    public function supported(string $key, string $cipher): bool
    {
        $length = mb_strlen($key, '8bit');

        return ($cipher === 'AES-128-CBC' && $length === 16) ||
            ($cipher === 'AES-256-CBC' && $length === 32);
    }

    /**
     * 加密
     *
     * @param   mixed   $value      需要加密的值
     * @param   bool    $serialize  是否进行序列化操作
     *
     * @return  string              加密后的字段
     */
    public function encrypt($value, bool $serialize = false): string
    {
        $iv = openssl_random_pseudo_bytes(openssl_cipher_iv_length($this->cipher));
        $value = \openssl_encrypt(
            $serialize ? serialize($value) : $value,
            $this->cipher,
            $this->key,
            0,
            $iv
        );

        if ($value === false) {
            throw new \RuntimeException('Could not encrypt the data.');
        }
        $iv = base64_encode($iv);
        $mac = hash_hmac('sha256', $iv . $value, $this->key);
        $json = json_encode(compact('iv', 'value', 'mac'), JSON_UNESCAPED_SLASHES);
        if (json_last_error() !== JSON_ERROR_NONE) {
            throw new \RuntimeException('Could not encrypt the data.');
        }
        return base64_encode($json);
    }

    /**
     * 解密
     *
     * @param   string   $payload      加密后的字段
     * @param   bool     $unserialize  是否进行反序列化操作
     *
     * @return  mixed|void             解密后的数据
     */
    public function decrypt(string $payload, bool $unserialize = false)
    {
        $payload = $payload = json_decode(base64_decode($payload), true);
        if (!$this->vaild($payload)) {
            return;
        }
        $iv = base64_decode($payload['iv']);
        $decrypted = \openssl_decrypt(
            $payload['value'],
            $this->cipher,
            $this->key,
            0,
            $iv
        );

        if ($decrypted === false) {
            throw new \RuntimeException('Could not decrypt the data.');
        }

        return $unserialize ? unserialize($decrypted) : $decrypted;
    }

    /**
     * 验证加密字段是否合法
     *
     * @param   mixed  $payload   加密后的字段
     *
     * @return  bool              是否合法
     */
    public function vaild($payload)
    {
        if (
            !is_array($payload) || !isset($payload['iv'], $payload['value'], $payload['mac']) ||
            strlen(base64_decode($payload['iv'], true)) !== openssl_cipher_iv_length($this->cipher)
        ) {
            throw new \RuntimeException('The payload is invalid.');
        }
        if (!hash_equals(
            $payload['mac'],
            hash_hmac('sha256', $payload['iv'] . $payload['value'], $this->key)
        )) {
            throw new \RuntimeException('The MAC is invalid.');
        }
        return true;
    }

    public function encryptSerialize($value)
    {
        return $this->encrypt($value, true);
    }

    public function decryptSerialize($value)
    {
        return $this->decrypt($value, true);
    }

    public function encryptString(string $value): string
    {
        return $this->encrypt($value, false);
    }

    public function decryptString(string $value): string
    {
        return $this->decrypt($value, false);
    }
}
