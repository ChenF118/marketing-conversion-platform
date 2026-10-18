# Start Vue frontend on port 8080
# Usage: powershell -ExecutionPolicy Bypass -File start-frontend.ps1
$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$proj = Join-Path $root 'Fans_Vue'

if (-not (Test-Path (Join-Path $proj 'node_modules'))) {
    Write-Host '[...] installing npm dependencies'
    Push-Location $proj
    npm install
    Pop-Location
}

$busy = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if ($busy) { throw 'Port 8080 is already in use. Run stop-all.ps1 first.' }

Write-Host '[OK] frontend starting: http://localhost:8080'
Set-Location $proj
npm run serve
