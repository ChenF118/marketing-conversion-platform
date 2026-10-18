# Start Java backend on port 8000
# Usage: powershell -ExecutionPolicy Bypass -File start-backend.ps1 [-Rebuild]
param([switch]$Rebuild)

$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$proj = Join-Path $root 'Fans_java'
$jar  = Join-Path $proj 'target\fans.jar'
$mvn  = (Get-Command mvn.cmd -ErrorAction SilentlyContinue).Source
if (-not $mvn) { $mvn = 'mvn.cmd' }
$java = (Get-Command java.exe -ErrorAction SilentlyContinue).Source
if (-not $java) { $java = 'java.exe' }

# 0) load .env into process environment (kept out of git)
$envFile = Join-Path $root '.env'
if (Test-Path $envFile) {
    Get-Content $envFile -Encoding UTF8 | ForEach-Object {
        if ($_ -match '^\s*([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*)\s*$') {
            $name = $matches[1]
            $value = $matches[2].Trim()
            if (-not [Environment]::GetEnvironmentVariable($name, 'Process')) {
                [Environment]::SetEnvironmentVariable($name, $value, 'Process')
            }
        }
    }
}

# 1) MySQL service
$svc = Get-Service -Name MySQL -ErrorAction SilentlyContinue
if ($svc -and $svc.Status -ne 'Running') {
    Write-Host '[...] starting MySQL service'
    Start-Service MySQL
    Start-Sleep -Seconds 3
}
if (-not (Test-NetConnection -ComputerName 127.0.0.1 -Port 3306 -InformationLevel Quiet -WarningAction SilentlyContinue)) {
    throw 'MySQL is not reachable on 127.0.0.1:3306'
}

# 2) port check
$busy = Get-NetTCPConnection -LocalPort 8000 -State Listen -ErrorAction SilentlyContinue
if ($busy) { throw 'Port 8000 is already in use. Run stop-all.ps1 first.' }

# 3) build when needed
$needBuild = $Rebuild -or (-not (Test-Path $jar))
if (-not $needBuild) {
    $jarTime = (Get-Item $jar).LastWriteTime
    $files = @(Get-ChildItem (Join-Path $proj 'src') -Recurse -File) + @(Get-Item (Join-Path $proj 'pom.xml'))
    $needBuild = $null -ne ($files | Where-Object { $_.LastWriteTime -gt $jarTime } | Select-Object -First 1)
}
if ($needBuild) {
    Write-Host '[...] building (mvn package)'
    & $mvn -f $proj -q -DskipTests package
    if ($LASTEXITCODE -ne 0) { throw 'mvn package failed' }
}

# 4) start
Write-Host '[OK] backend starting: http://127.0.0.1:8000'
& $java "-Dfile.encoding=UTF-8" -jar $jar
