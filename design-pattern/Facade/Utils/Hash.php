<?php

namespace Utils;

class Hash
{
    /**
     * 默认的设置
     *
     * @var array
     */
    protected $options = [];

    /**
     * 默认的算法
     *
     * @var int|string
     */
    protected $algo = PASSWORD_BCRYPT;

    /**
     * 制作 Hash
     *
     * @param   string      $value    需要 Hash 的字段
     * @param   int|string  $algo     Hash 算法
     * @param   array       $options  选项
     *
     * @return  string                Hash
     */
    public function make(string $value, $algo = null, array $options = null): string
    {
        $hash = password_hash($value, $algo ?? $this->algo, $options ?? $this->options);

        if ($hash === false) {
            throw new \RuntimeException('Algo hashing not supported.');
        }

        return $hash;
    }

    /**
     * 验证 Hash
     *
     * @param   string  $value         原始数据
     * @param   string  $hashed_value  Hash 值
     *
     * @return  bool                   Hash 是否正确
     */
    public function check(string $value, string $hashed_value): bool
    {
        return password_verify($value, $hashed_value);
    }

    /**
     * Hash 是否需要刷新
     *
     * @param   string      $hashed_value  Hash 值
     * @param   int|string  $algo          Hash 算法
     * @param   array       $options       选项
     *
     * @return  bool                       Hash 是否需要刷新
     */
    public function needsRehash(string $hashed_value, $algo = null, array $options = null): bool
    {
        return password_needs_rehash($hashed_value, $algo ?? $this->algo, $options ?? $this->options);
    }
}
