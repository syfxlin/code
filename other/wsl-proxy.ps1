$wsl = "Ubuntu-WSL2"
$dir = "C:\Users\syfxl\scoop\wsl-proxy";

if ($args[0] -eq "add") {
    if (!$args[2].ToString().Contains('$args')) {
        $args[2] += ' $args';
    }
    $filename = $args[1];
    echo ("wsl -d $wsl -- " + $args[2]) | Out-File $dir\$filename.ps1;
}
if ($args[0] -eq "del") {
    $filename = $args[1];
    Remove-Item $dir\$filename.ps1
}