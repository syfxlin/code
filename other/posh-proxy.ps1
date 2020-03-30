$defalut_proxy = 'http://127.0.0.1:7890';

if ([System.Environment]::GetEnvironmentVariable('DEFAULT_PROXY') -ne $null) {
    $defalut_proxy = [System.Environment]::GetEnvironmentVariable('DEFAULT_PROXY');
}

function Env-Proxy($operate, $proxy = $defalut_proxy) {
    if ($operate -eq "get") {
        return $env:HTTP_PROXY;
    }
    elseif ($operate -eq "set") {
        $env:HTTP_PROXY = $proxy;
        $env:HTTPS_PROXY = $proxy;
        return "Success";
    }
    elseif ($operate -eq "del") {
        if ($env:HTTP_PROXY -ne $null) {
            Remove-Item env:HTTP_PROXY
            Remove-Item env:HTTPS_PROXY
        }
        return "Success";
    }
}

function Git-Proxy($operate, $proxy = $defalut_proxy) {
    if ($operate -eq "get") {
        return (git config --global --get http.proxy);
    }
    elseif ($operate -eq "set") {
        git config --global http.proxy $proxy;
        git config --global https.proxy $proxy;
        return "Success";
    }
    elseif ($operate -eq "del") {
        git config --global --unset http.proxy
        git config --global --unset https.proxy
        return "Success";
    }
}

function Node-Proxy($operate, $proxy = $defalut_proxy) {
    if ($operate -eq "get") {
        $yarn_proxy = (yarn config get proxy);
        if ($yarn_proxy -eq "undefined") {
            return $null;
        }
        else {
            return $yarn_proxy;
        }
    }
    elseif ($operate -eq "set") {
        npm config set proxy $proxy | Out-Null;
        npm config set https-proxy $proxy | Out-Null;
        yarn config set proxy $proxy | Out-Null;
        yarn config set https-proxy $proxy | Out-Null;
        return "Success";
    }
    elseif ($operate -eq "del") {
        npm config delete proxy | Out-Null
        npm config delete https-proxy | Out-Null
        yarn config delete proxy | Out-Null
        yarn config delete https-proxy | Out-Null
        return "Success";
    }
}

function Composer-Proxy($operate, $proxy = $defalut_proxy) {
    return (Env-Proxy $operate $proxy);
}

function Scoop-Proxy($operate, $proxy = $defalut_proxy) {
    if ($operate -eq "get") {
        $scoop_proxy = (scoop config proxy);
        if ($scoop_proxy -eq "'proxy' is not set") {
            return $null;
        }
        else {
            return $scoop_proxy;
        }
    }
    elseif ($operate -eq "set") {
        scoop config proxy $proxy.Substring(7) | Out-Null
        return "Success";
    }
    elseif ($operate -eq "del") {
        scoop config rm proxy | Out-Null
        return "Success";
    }
}

# 脚本部分
if ($args[0] -in "set", "get", "del") {
    $used_proxy = $defalut_proxy;
    if ($args[2] -ne $null) {
        $used_proxy = $args[2];
    }
    if ($args[1] -eq "env") {
        Write-Output (Env-Proxy $args[0] $used_proxy);
    }
    if ($args[1] -eq "git") {
        Write-Output (Git-Proxy $args[0] $used_proxy);
    }
    if ($args[1] -in "npm", "node", "yarn") {
        Write-Output (Node-Proxy $args[0] $used_proxy);
    }
    if ($args[1] -eq "composer") {
        Write-Output (Composer-Proxy $args[0] $used_proxy);
    }
    if ($args[1] -eq "all" -or $args[1] -eq $null) {
        Write-Output ("Env: " + (Env-Proxy $args[0] $used_proxy));
        Write-Output ("Git: " + (Git-Proxy $args[0] $used_proxy));
        Write-Output ("Node: " + (Node-Proxy $args[0] $used_proxy));
        Write-Output ("Composer: " + (Composer-Proxy $args[0] $used_proxy));
        Write-Output ("Scoop: " + (Scoop-Proxy $args[0] $used_proxy));
    }
    if ($args[1] -eq "proxy") {
        if ($args[0] -eq "set") {
            [System.Environment]::SetEnvironmentVariable('DEFAULT_PROXY', $args[2], [System.EnvironmentVariableTarget]::User);
        }
        elseif ($args[0] -eq "get") {
            [System.Environment]::GetEnvironmentVariable('DEFAULT_PROXY', [System.EnvironmentVariableTarget]::User);
        }
        elseif ($args[0] -eq "del") {
            [System.Environment]::SetEnvironmentVariable('DEFAULT_PROXY', '', [System.EnvironmentVariableTarget]::User);
        }
    }
}
